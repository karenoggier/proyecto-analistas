/** @type {import('next').NextConfig} */
const nextConfig = {
  typescript: {
    ignoreBuildErrors: true,
  },
  images: {
    unoptimized: true,
  },
  
  async rewrites() {
    return [
      {
        // 1. Todo lo que sea de USUARIOS -> Al Gateway (8090)
        source: '/usuariosMs/:path*',
        destination: 'http://127.0.0.1:8090/usuariosMs/:path*', 
      },
      {
        // 2. Todo lo que sea de CATÁLOGO -> Al Gateway (8090)
        source: '/catalogoMs/:path*',
        destination: 'http://localhost:8090/catalogoMs/:path*', 
      },
    ]
  },
}

export default nextConfig