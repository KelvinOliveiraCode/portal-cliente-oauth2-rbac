import { useEffect, useState } from 'react';
import { listarUsuarios, listarPapeis } from '../api';
import type { Usuario } from '../api';

type Props = {
  user: { username: string; roles: string[] };
  token: string;
};

const isAdmin = (roles: string[]) => roles.includes('ADMIN');

export function Dashboard({ user, token }: Props) {
  const admin = isAdmin(user.roles);
  const [usuarios, setUsuarios] = useState<Usuario[]>([]);
  const [papeis, setPapeis] = useState<any[]>([]);
  const [erro, setErro] = useState<string | null>(null);

  useEffect(() => {
    const promises: Promise<void>[] = [];
    if (admin) {
      promises.push(
        listarUsuarios(token).then(setUsuarios).catch(() => setErro('Falha ao carregar usuários')),
        listarPapeis(token).then(setPapeis).catch(() => setErro('Falha ao carregar papéis')),
      );
    } else {
      promises.push(
        listarPapeis(token).then(setPapeis).catch(() => setErro('Falha ao carregar papéis')),
      );
    }
    Promise.all(promises);
  }, [token, admin]);

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div className="flex items-center justify-between mb-8">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Painel do cliente</h1>
          <p className="text-gray-600">Bem-vindo, {user.username}</p>
        </div>
        <span className="px-3 py-1 rounded-full bg-indigo-100 text-indigo-700 text-xs font-semibold">
          {user.roles.join(', ')}
        </span>
      </div>

      {erro && (
        <div className="mb-6 p-3 rounded border border-red-200 bg-red-50 text-red-800 text-sm">
          {erro}
        </div>
      )}

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-200">
          <div className="flex items-center justify-between mb-4">
            <div>
              <p className="text-sm text-gray-500">Usuários cadastrados</p>
              <p className="text-2xl font-bold text-gray-900">{usuarios.length}</p>
            </div>
            <span className="text-gray-400">👥</span>
          </div>
          {admin ? (
            <ul className="space-y-2 text-sm">
              {usuarios.map((u) => (
                <li key={u.id} className="flex items-center justify-between p-2 rounded hover:bg-gray-50">
                  <span>{u.username}</span>
                  <span className="text-[10px] px-2 py-0.5 rounded bg-gray-100 text-gray-600">
                    {u.ativo ? 'ativo' : 'inativo'}
                  </span>
                </li>
              ))}
              {usuarios.length === 0 && <p className="text-gray-400 text-sm">Nenhum usuário.</p>}
            </ul>
          ) : (
            <p className="text-sm text-gray-500">Acesso restrito a ADMIN.</p>
          )}
        </div>

        <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-200">
          <div className="flex items-center justify-between mb-4">
            <div>
              <p className="text-sm text-gray-500">Papéis do sistema (RBAC)</p>
              <p className="text-2xl font-bold text-gray-900">{papeis.length}</p>
            </div>
            <span className="text-gray-400">🏷️</span>
          </div>
          <ul className="space-y-2 text-sm">
            {papeis.map((p) => (
              <li key={p.id} className="flex items-center justify-between p-2 rounded hover:bg-gray-50">
                <span>{p.nome}</span>
                <span className="text-[10px] px-2 py-0.5 rounded bg-gray-100 text-gray-600">
                  {p.descricao || '—'}
                </span>
              </li>
            ))}
            {papeis.length === 0 && <p className="text-gray-400 text-sm">Nenhum papel.</p>}
          </ul>
        </div>

        <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-200">
          <div className="flex items-center justify-between mb-4">
            <div>
              <p className="text-sm text-gray-500">Seu papel</p>
              <p className="text-2xl font-bold text-gray-900">
                {admin ? 'ADMINISTRADOR' : 'CLIENTE'}
              </p>
            </div>
            <span className="text-indigo-600">🛡️</span>
          </div>
          <p className="text-sm text-gray-600 mb-4">
            {admin
              ? 'Acesso total: gerencia usuários, papéis e recursos administrativos.'
              : 'Acesso ao painel do cliente e recursos de cliente.'}
          </p>
          {admin && (
            <a href="#/usuarios" className="text-indigo-600 text-sm font-medium hover:underline">
              Gerenciar usuários →
            </a>
          )}
        </div>
      </div>
    </div>
  );
}
