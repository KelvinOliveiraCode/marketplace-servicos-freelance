import { useState, useEffect, FormEvent } from 'react';
import {
  listarAnuncios,
  criarAnuncio,
  Anuncio,
  CreateAnuncio,
} from './api';
import AnuncioCard from './components/AnuncioCard';

type Modo = 'listar' | 'criar';

export default function App() {
  const [anuncios, setAnuncios] = useState<Anuncio[]>([]);
  const [modo, setModo] = useState<Modo>('listar');
  const [loading, setLoading] = useState(false);
  const [erro, setErro] = useState<string | null>(null);
  const [titulo, setTitulo] = useState('');
  const [descricao, setDescricao] = useState('');
  const [preco, setPreco] = useState('');
  const [categoria, setCategoria] = useState('');
  const [prestadorId, setPrestadorId] = useState('');

  useEffect(() => {
    carregar();
  }, []);

  async function carregar() {
    setLoading(true);
    setErro(null);
    try {
      const lista = await listarAnuncios();
      setAnuncios(lista);
    } catch (e) {
      setErro(e instanceof Error ? e.message : 'Erro ao carregar anúncios');
    } finally {
      setLoading(false);
    }
  }

  async function handleCriarAnuncio(ev: FormEvent) {
    ev.preventDefault();
    setErro(null);
    const body: CreateAnuncio = {
      titulo,
      descricao,
      preco: Number(preco),
      categoria,
      prestadorId,
    };
    try {
      const novo = await criarAnuncio(body);
      setAnuncios((atual) => [novo, ...atual]);
      setTitulo('');
      setDescricao('');
      setPreco('');
      setCategoria('');
      setPrestadorId('');
      setModo('listar');
    } catch (e) {
      setErro(e instanceof Error ? e.message : 'Erro ao criar anúncio');
    }
  }

  if (loading) {
    return <div className="p-4 text-center">Carregando...</div>;
  }
  if (erro) {
    return <div className="p-4 text-red-600">{erro}</div>;
  }

  return (
    <div className="max-w-5xl mx-auto p-4">
      <h1 className="text-2xl font-bold mb-4">Marketplace de Serviços Freelance</h1>

      {modo === 'criar' ? (
        <div className="mb-6 bg-white p-4 rounded shadow border">
          <h2 className="text-lg font-semibold mb-2">Publicar novo anúncio</h2>
          <form onSubmit={handleCriarAnuncio} className="space-y-3">
            <input
              className="border p-2 w-full"
              placeholder="Título"
              value={titulo}
              onChange={(e) => setTitulo(e.target.value)}
            />
            <input
              className="border p-2 w-full"
              placeholder="Descrição"
              value={descricao}
              onChange={(e) => setDescricao(e.target.value)}
            />
            <input
              className="border p-2 w-full"
              type="number"
              placeholder="Preço"
              value={preco}
              onChange={(e) => setPreco(e.target.value)}
            />
            <input
              className="border p-2 w-full"
              placeholder="Categoria"
              value={categoria}
              onChange={(e) => setCategoria(e.target.value)}
            />
            <input
              className="border p-2 w-full"
              placeholder="ID do prestador"
              value={prestadorId}
              onChange={(e) => setPrestadorId(e.target.value)}
            />
            <button type="submit" className="bg-blue-600 text-white px-4 py-2 rounded">
              Publicar
            </button>
            <button
              type="button"
              onClick={() => setModo('listar')}
              className="text-sm underline"
            >
              Voltar
            </button>
          </form>
        </div>
      ) : null}

      <div className="flex justify-between items-center mb-4">
        <h2 className="text-lg font-semibold">Anúncios disponíveis</h2>
        <button
          onClick={() => setModo('criar')}
          className="bg-blue-600 text-white px-4 py-2 rounded"
        >
          + Novo anúncio
        </button>
      </div>

      {anuncios.length === 0 ? (
        <p className="text-gray-500">Nenhum anúncio publicado ainda.</p>
      ) : (
        <div className="grid gap-4">
          {anuncios.map((a) => (
            <AnuncioCard key={String(a.id)} anuncio={a} onAtualizar={carregar} />
          ))}
        </div>
      )}
    </div>
  );
}
