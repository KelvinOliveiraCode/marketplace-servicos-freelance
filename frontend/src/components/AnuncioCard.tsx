import { useState, useEffect, FormEvent } from 'react';
import {
  listarPropostas,
  enviarProposta,
  aceitarProposta,
  contratar,
  Proposta,
  CreateProposta,
} from '../api';

interface AnuncioResumo {
  id: number;
  titulo: string;
  descricao: string;
  preco: string;
  categoria: string;
  prestadorId: string;
  status: string;
}

interface Props {
  anuncio: AnuncioResumo;
  onAtualizar: () => void;
}

export default function AnuncioCard({ anuncio, onAtualizar }: Props) {
  const [propostas, setPropostas] = useState<Proposta[]>([]);
  const [loading, setLoading] = useState(false);
  const [erro, setErro] = useState<string | null>(null);
  const [aberta, setAberta] = useState(false);
  const [clienteId, setClienteId] = useState('');
  const [descricao, setDescricao] = useState('');
  const [precoProposto, setPrecoProposto] = useState('');

  useEffect(() => {
    carregarPropostas();
  }, [anuncio.id]);

  async function carregarPropostas() {
    setLoading(true);
    setErro(null);
    try {
      const lista = await listarPropostas(anuncio.id);
      setPropostas(lista);
    } catch (e) {
      setErro(e instanceof Error ? e.message : 'Erro ao carregar propostas');
    } finally {
      setLoading(false);
    }
  }

  async function handleEnviarProposta(ev: FormEvent) {
    ev.preventDefault();
    setErro(null);
    const body: CreateProposta = {
      anuncioId: anuncio.id,
      clienteId,
      descricao,
      precoProposto: Number(precoProposto),
    };
    try {
      await enviarProposta(body);
      setClienteId('');
      setDescricao('');
      setPrecoProposto('');
      await carregarPropostas();
      onAtualizar();
    } catch (e) {
      setErro(e instanceof Error ? e.message : 'Erro ao enviar proposta');
    }
  }

  async function handleAceitar(id: number) {
    try {
      await aceitarProposta(id);
      await carregarPropostas();
      onAtualizar();
    } catch (e) {
      setErro(e instanceof Error ? e.message : 'Erro ao aceitar proposta');
    }
  }

  async function handleContratar(propostaId: number) {
    try {
      await contratar({ propostaId });
      await carregarPropostas();
      onAtualizar();
    } catch (e) {
      setErro(e instanceof Error ? e.message : 'Erro ao contratar');
    }
  }

  const statusClass = (status: string) => {
    if (status === 'PENDENTE') return 'bg-yellow-100 text-yellow-700';
    if (status === 'ACEITA') return 'bg-green-100 text-green-700';
    if (status === 'REJEITADA') return 'bg-red-100 text-red-700';
    return 'bg-gray-100 text-gray-700';
  };

  const anuncioStatusClass = () => {
    if (anuncio.status === 'DISPONIVEL') return 'bg-green-100 text-green-700';
    if (anuncio.status === 'CONTRATADO') return 'bg-red-100 text-red-700';
    return 'bg-gray-100 text-gray-700';
  };

  return (
    <div className="border p-4 rounded bg-white shadow">
      <div className="flex justify-between items-start mb-2">
        <div>
          <h3 className="text-lg font-bold">{anuncio.titulo}</h3>
          <p className="text-gray-600 text-sm">{anuncio.descricao}</p>
        </div>
        <span className={`text-xs font-semibold px-2 py-1 rounded ${anuncioStatusClass()}`}>
          {anuncio.status}
        </span>
      </div>
      <p className="text-sm mb-3">
        R$ {anuncio.preco} &middot; {anuncio.categoria} &middot;
        prestador: {anuncio.prestadorId}
      </p>

      <button
        onClick={() => setAberta(!aberta)}
        className="text-sm text-blue-600 underline mb-3"
      >
        {aberta ? 'Ocultar propostas' : 'Ver propostas'}
      </button>

      {aberta && (
        <div className="space-y-3">
          <h4 className="text-sm font-semibold">Nova proposta</h4>
          <form onSubmit={handleEnviarProposta} className="space-y-2">
            <input
              className="border p-2 w-full"
              placeholder="Seu ID (cliente)"
              value={clienteId}
              onChange={(e) => setClienteId(e.target.value)}
            />
            <input
              className="border p-2 w-full"
              placeholder="Descrição da proposta"
              value={descricao}
              onChange={(e) => setDescricao(e.target.value)}
            />
            <input
              className="border p-2 w-full"
              type="number"
              placeholder="Preço proposto"
              value={precoProposto}
              onChange={(e) => setPrecoProposto(e.target.value)}
            />
            <button
              type="submit"
              className="bg-blue-600 text-white px-4 py-2 rounded w-auto"
            >
              Enviar proposta
            </button>
          </form>

          {erro && <p className="text-sm text-red-600">{erro}</p>}

          <h4 className="text-sm font-semibold mt-2">Propostas recebidas</h4>
          {loading ? (
            <p className="text-sm">Carregando...</p>
          ) : propostas.length === 0 ? (
            <p className="text-sm text-gray-500">Sem propostas ainda.</p>
          ) : (
            <ul className="space-y-2">
              {propostas.map((p) => (
                <li key={String(p.id)} className="flex items-center justify-between text-sm border-b pb-2">
                  <div>
                    <div className="font-medium">{p.clienteId}</div>
                    <div className="text-xs text-gray-500">R$ {p.precoProposto}</div>
                    <div className="text-xs">{p.descricao}</div>
                    <span className={`text-[10px] font-semibold px-1.5 rounded ${statusClass(p.status)}`}>
                      {p.status}
                    </span>
                  </div>
                  <div className="flex gap-2">
                    {p.status === 'PENDENTE' && (
                      <button
                        onClick={() => handleAceitar(p.id)}
                        className="text-xs bg-green-600 text-white px-2 py-1 rounded"
                      >
                        Aceitar
                      </button>
                    )}
                    {p.status === 'ACEITA' && (
                      <button
                        onClick={() => handleContratar(p.id)}
                        className="text-xs bg-blue-600 text-white px-2 py-1 rounded"
                      >
                        Contratar
                      </button>
                    )}
                  </div>
                </li>
              ))}
            </ul>
          )}
        </div>
      )}
    </div>
  );
}
