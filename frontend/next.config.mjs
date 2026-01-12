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
        // El frontend escuchará peticiones que empiecen con /usuariosMs
        source: '/usuariosMs/:path*',
        // Y las redirigirá internamente a tu backend en Java (sin problemas de CORS)
        destination: 'http://localhost:8080/usuariosMs/:path*', 
      },
    ]
  },
}

export default nextConfig