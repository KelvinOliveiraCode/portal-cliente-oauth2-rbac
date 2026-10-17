import type { AuthResponse, Usuario } from './types';

export type { AuthResponse, Usuario } from './types';

const API_BASE = '';

function headers(token?: string): HeadersInit {
  const h: HeadersInit = { 'Content-Type': 'application/json' };
  if (token) h.Authorization = `Bearer ${token}`;
  return h;
}

export async function login(username: string, senha: string): Promise<AuthResponse> {
  const resposta = await fetch(`${API_BASE}/auth/login`, {
    method: 'POST',
    headers: headers(),
    body: JSON.stringify({ username, senha }),
  });
  if (!resposta.ok) throw new Error('Usuário ou senha inválidos');
  return resposta.json();
}

export async function getMe(token: string): Promise<{ username: string; roles: string[] }> {
  const resposta = await fetch(`${API_BASE}/auth/me`, { headers: headers(token) });
  if (!resposta.ok) throw new Error('Não autenticado');
  return resposta.json();
}

export async function listarUsuarios(token: string): Promise<Usuario[]> {
  const resposta = await fetch(`${API_BASE}/api/usuarios`, { headers: headers(token) });
  if (!resposta.ok) throw new Error('Não autorizado');
  return resposta.json();
}

export async function listarPapeis(token: string): Promise<any[]> {
  const resposta = await fetch(`${API_BASE}/api/papeis`, { headers: headers(token) });
  if (!resposta.ok) throw new Error('Não autorizado');
  return resposta.json();
}
