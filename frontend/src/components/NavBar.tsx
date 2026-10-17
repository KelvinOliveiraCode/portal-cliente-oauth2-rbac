type Props = {
  user: { username: string; roles: string[] };
  onLogout: () => void;
};

export function NavBar({ user, onLogout }: Props) {
  const isAdmin = user.roles.includes('ADMIN');

  return (
    <nav className="bg-white border-b border-gray-200 sticky top-0 z-10">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          <div className="flex items-center gap-2">
            <span className="text-indigo-600">🏛️</span>
            <span className="font-semibold text-gray-900">Portal Cliente</span>
          </div>

          <div className="flex items-center gap-4">
            <div className="hidden md:flex items-center gap-2 text-sm text-gray-600">
              <span>{user.username}</span>
              <span className="px-2 py-0.5 rounded text-[10px] font-medium bg-gray-100 text-gray-500">
                {user.roles.join(' • ')}
              </span>
            </div>

            {isAdmin && (
              <a href="#/usuarios" className="text-sm font-medium text-indigo-600 hover:text-indigo-700">
                Usuários
              </a>
            )}
            <a href="#/papeis" className="text-sm font-medium text-indigo-600 hover:text-indigo-700">
              Papéis
            </a>

            <button
              onClick={onLogout}
              className="flex items-center gap-2 text-sm font-medium text-red-600 hover:text-red-700"
            >
              Sair
            </button>
          </div>
        </div>
      </div>
    </nav>
  );
}
