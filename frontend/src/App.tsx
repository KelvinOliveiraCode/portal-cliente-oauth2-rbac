import { useCallback, useEffect, useState } from 'react';
import { LoginForm } from './components/LoginForm';
import { NavBar } from './components/NavBar';
import { Dashboard } from './pages/Dashboard';
import { login, getMe } from './api';
import type { AuthResponse } from './api';

type UserSession = { username: string; roles: string[] };

function App() {
  const [token, setToken] = useState<string | null>(null);
  const [user, setUser] = useState<UserSession | null>(null);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const saved = localStorage.getItem('token');
    const savedUser = localStorage.getItem('user');
    if (saved && savedUser) {
      setToken(saved);
      setUser(JSON.parse(savedUser));
    }
  }, []);

  const handleLogin = useCallback(async (username: string, senha: string) => {
    setError(null);
    try {
      const resposta: AuthResponse = await login(username, senha);
      const me = await getMe(resposta.token);
      localStorage.setItem('token', resposta.token);
      localStorage.setItem('user', JSON.stringify(me));
      setToken(resposta.token);
      setUser(me);
    } catch (e) {
      setError(e instanceof Error ? e.message : 'Erro ao entrar');
    }
  }, []);

  const handleLogout = useCallback(() => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    setToken(null);
    setUser(null);
  }, []);

  if (!token || !user) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-900">
        <div className="w-full max-w-md">
          <LoginForm onLogin={handleLogin} error={error} />
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <NavBar user={user} onLogout={handleLogout} />
      <Dashboard user={user} token={token} />
    </div>
  );
}

export default App;
