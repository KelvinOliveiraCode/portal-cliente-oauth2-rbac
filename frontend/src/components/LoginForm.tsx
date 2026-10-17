import { useState } from 'react';

type Props = {
  onLogin: (username: string, senha: string) => Promise<void>;
  error: string | null;
};

export function LoginForm({ onLogin, error }: Props) {
  const [username, setUsername] = useState('');
  const [senha, setSenha] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = (event: React.FormEvent) => {
    event.preventDefault();
    setLoading(true);
    onLogin(username, senha).finally(() => setLoading(false));
  };

  return (
    <div className="bg-white p-8 rounded-xl shadow-sm border border-gray-200">
      <div className="flex items-center gap-2 mb-6">
        <span className="text-indigo-600">🔐</span>
        <h1 className="text-xl font-semibold">Entrar no portal</h1>
      </div>

      {error && (
        <div className="mb-6 p-3 rounded border border-red-200 bg-red-50 text-red-800 text-sm flex items-center gap-2">
          <span>⚠️</span> {error}
        </div>
      )}

      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Usuário</label>
          <input
            className="w-full px-3 py-2 border border-gray-300 rounded focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 outline-none"
            placeholder="ex: admin"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Senha</label>
          <div className="relative">
            <input
              type="password"
              className="w-full px-3 py-2 border border-gray-300 rounded pr-10 focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 outline-none"
              placeholder="••••••••"
              value={senha}
              onChange={(e) => setSenha(e.target.value)}
            />
            <span className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400">📧</span>
          </div>
        </div>
        <button
          type="submit"
          disabled={loading}
          className="w-full px-4 py-2 bg-indigo-600 hover:bg-indigo-700 disabled:bg-indigo-200 text-white font-medium rounded transition"
        >
          {loading ? 'Entrando...' : 'Entrar'}
        </button>
      </form>
    </div>
  );
}
