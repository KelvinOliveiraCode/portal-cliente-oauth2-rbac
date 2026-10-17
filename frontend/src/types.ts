export type Role = 'ADMIN' | 'CLIENTE';

export interface Usuario {
  id: number;
  username: string;
  email: string;
  ativo: boolean;
  roles: string[];
}

export interface AuthResponse {
  token: string;
  username: string;
  roles: string[];
}

export interface LoginRequest {
  username: string;
  senha: string;
}
