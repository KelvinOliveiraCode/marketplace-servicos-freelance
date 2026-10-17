const BASE = 'http://localhost:8080';

async function request(path: string, options: RequestInit = {}): Promise<Response> {
  const res = await fetch(`${BASE}${path}`, {
    headers: { 'Content-Type': 'application/json', ...options.headers },
    ...options,
  });
  if (!res.ok) {
    const body = await res.text().catch(() => '');
    throw new Error(`${res.status} ${res.statusText}${body ? ': ' + body : ''}`);
  }
  return res;
}

export interface Anuncio {
  id: number;
  titulo: string;
  descricao: string;
  preco: string;
  categoria: string;
  prestadorId: string;
  status: string;
}

export interface CreateAnuncio {
  titulo: string;
  descricao: string;
  preco: number;
  categoria: string;
  prestadorId: string;
}

export interface Proposta {
  id: number;
  anuncioId: number;
  clienteId: string;
  descricao: string;
  precoProposto: string;
  status: string;
}

export interface CreateProposta {
  anuncioId: number;
  clienteId: string;
  descricao: string;
  precoProposto: number;
}

export interface Contratacao {
  id: number;
  anuncioId: number;
  propostaId: number;
  prestadorId: string;
  clienteId: string;
  status: string;
}

export interface CreateContratacao {
  propostaId: number;
}

export async function listarAnuncios(): Promise<Anuncio[]> {
  const res = await request('/anuncios');
  return (await res.json()) as Anuncio[];
}

export async function criarAnuncio(body: CreateAnuncio): Promise<Anuncio> {
  const res = await request('/anuncios', {
    method: 'POST',
    body: JSON.stringify(body),
  });
  return (await res.json()) as Anuncio;
}

export async function listarPropostas(anuncioId: number): Promise<Proposta[]> {
  const res = await request(`/propostas/anuncios/${anuncioId}`);
  return (await res.json()) as Proposta[];
}

export async function enviarProposta(body: CreateProposta): Promise<Proposta> {
  const res = await request('/propostas', {
    method: 'POST',
    body: JSON.stringify(body),
  });
  return (await res.json()) as Proposta;
}

export async function aceitarProposta(propostaId: number): Promise<Proposta> {
  const res = await request(`/propostas/${propostaId}/aceitar`, {
    method: 'POST',
  });
  return (await res.json()) as Proposta;
}

export async function contratar(body: CreateContratacao): Promise<Contratacao> {
  const res = await request('/contratacoes', {
    method: 'POST',
    body: JSON.stringify(body),
  });
  return (await res.json()) as Contratacao;
}
