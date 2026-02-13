'use client';

import { useEffect, useRef, useState } from 'react';
import Image from 'next/image';
import Navbar from './components/Navbar';
import Footer from './components/Footer';
import styles from './cliente.module.css';

const categories = [
  { name: 'Hamburguesa', img: '/images/cat-hamburguesa.jpg' },
  { name: 'Ensalada', img: '/images/cat-ensalada.jpg' },
  { name: 'Gaseosa', img: '/images/cat-gaseosa.jpg' },
  { name: 'Pizza', img: '/images/cat-pizza.jpg' },
  { name: 'Helado', img: '/images/cat-helado.jpg' },
  { name: 'Vino', img: '/images/cat-vino.jpg' },
  { name: 'Cafe', img: '/images/cat-cafe.jpg' },
];

const stores = [
  { name: "McDonald's Shopping", time: '10-30 min', travel: '5-20 min' },
  { name: "McDonald's Shopping", time: '10-30 min', travel: '5-20 min' },
  { name: "McDonald's Shopping", time: '10-30 min', travel: '5-20 min' },
  { name: "McDonald's Shopping", time: '10-30 min', travel: '5-20 min' },
  { name: "McDonald's Shopping", time: '10-30 min', travel: '5-20 min' },
  { name: "McDonald's Shopping", time: '10-30 min', travel: '5-20 min' },
];

export default function ClienteHome() {
  const scrollRef = useRef(null);
  const [clientProfile, setClientProfile] = useState(null);

  const [canScrollLeft, setCanScrollLeft] = useState(false);
  const [canScrollRight, setCanScrollRight] = useState(false);

  useEffect(() => {
      fetchPerfil();
  
    }, [])

    const fetchPerfil = async () => {
      const token = sessionStorage.getItem("token")
      const rol = sessionStorage.getItem("rol")
  
      if (!token || rol !== "CLIENTE") {
        window.location.href = "/login"
        return
      }
        try {
          const headers = {
              'Authorization': `Bearer ${token}`,
              'Content-Type': 'application/json'
          };
  
          const [perfilRes] = await Promise.all([
              fetch('/pedidoMs/clientes/perfil', { method: 'GET', headers }),
          ]);
  
          if (perfilRes.status === 401 || perfilRes.status === 403) {
              sessionStorage.clear(); 
              window.location.href = "/login?expired=true"; 
              return;
          }
  
          if (perfilRes.ok) {
              const dataPerfil = await perfilRes.json();
              setClientProfile(dataPerfil);
          } else {
              console.error("Error al obtener perfil del cliente");
          }
  
        } catch (error) {
          console.error("Error de red:", error);
        } 
      }
  
    const handleLogout = () => {
      sessionStorage.clear()
      window.location.href = "/login"
    }
  
    const handleNavigate = (path) => {
      window.location.href = path
    }

    const handleRefreshProfile = () => {
      fetchPerfil();
    };
  /*
    // FUNCION PARA CARRUSSEL DE LOCALES
    const checkScroll = () => {
      const el = productsRef.current;
      if (!el) return;
  
      const hasOverflow = el.scrollWidth > el.clientWidth;
  
      setCanScrollLeft(hasOverflow && el.scrollLeft > 0);
      setCanScrollRight(hasOverflow && el.scrollLeft + el.clientWidth < el.scrollWidth);
    };*/

  const scrollLeft = () => {
    if (scrollRef.current) {
      scrollRef.current.scrollBy({ left: -280, behavior: 'smooth' });
    }
  };

  const scrollRight = () => {
    if (scrollRef.current) {
      scrollRef.current.scrollBy({ left: 280, behavior: 'smooth' });
    }
  };

  return (
    <div className={styles.page}>
      <Navbar showSearchBar profile={clientProfile} onAddressUpdate={handleRefreshProfile}/>

      <main className={styles.main}>
        {/* Hero Section */}
        <section className={styles.hero}>
          <div className={styles.heroInner}>
            <div className={styles.heroCardComida}>
              <span className={styles.heroLabelComida}>COMIDA</span>
              <Image
                src="/cliente/comida.png"
                alt="Comida"
                width={160}
                height={120}
                className={styles.heroImgComida}
              />
            </div>
            <div className={styles.heroCardBebida}>
              <Image
                src="/cliente/bebida.png"
                alt="Bebida"
                width={140}
                height={120}
                className={styles.heroImgBebida}
              />
              <span className={styles.heroLabelBebida}>BEBIDA</span>
            </div>
          </div>
        </section>

        {/* Categories */}
        <section className={styles.categories}>
          {categories.map((cat) => (
            <div key={cat.name} className={styles.categoryItem}>
              <div className={styles.categoryCircle}>
                <Image src={cat.img || "/placeholder.svg"} alt={cat.name} width={80} height={80} className={styles.categoryImg} />
              </div>
              <span className={styles.categoryName}>{cat.name}</span>
            </div>
          ))}
        </section>

        {/* Discover Section */}
        <section className={styles.discover}>
          <div className={styles.discoverHeader}>
            <h2 className={styles.discoverTitle}>Descubri estas opciones</h2>
            <div className={styles.discoverNav}>
              <button className={styles.navArrow} onClick={scrollLeft} aria-label="Scroll left">
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                  <path d="M15 18l-6-6 6-6" />
                </svg>
              </button>
              <button className={styles.navArrowActive} onClick={scrollRight} aria-label="Scroll right">
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="white" strokeWidth="2">
                  <path d="M9 18l6-6-6-6" />
                </svg>
              </button>
            </div>
          </div>

          <div className={styles.storeScroll} ref={scrollRef}>
            {stores.map((store, i) => (
              <div key={i} className={styles.storeCard}>
                <div className={styles.storeLogo}>
                  <div className={styles.storeLogoPlaceholder}>M</div>
                </div>
                <h3 className={styles.storeName}>{store.name}</h3>
                <div className={styles.storeMeta}>
                  <span className={styles.storeTime}>
                    <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="#888" strokeWidth="2">
                      <circle cx="12" cy="12" r="10" />
                      <polyline points="12 6 12 12 16 14" />
                    </svg>
                    {store.time}
                  </span>
                  <span className={styles.storeTravel}>
                    <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="#888" strokeWidth="2">
                      <circle cx="12" cy="12" r="10" />
                      <polyline points="12 6 12 12 16 14" />
                    </svg>
                    {store.travel}
                  </span>
                </div>
              </div>
            ))}
          </div>
        </section>

        {/* Footer Banner */}
        <section className={styles.footerBanner}>
          <div className={styles.bannerLeft}>
            <Image
              src="/cliente/chica-pizza.png"
              alt="Girl eating pizza"
              width={200}
              height={250}
              className={styles.bannerImg}
            />
          </div>
          <div className={styles.bannerRight}>
            <div className={styles.bannerBlock}>
              <div className={styles.bannerIcon}>
                <svg width="45" height="45" viewBox="0 0 24 24" fill="none" stroke="#ff4b7e" strokeWidth="2">
                  <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0118 0z" />
                  <circle cx="12" cy="10" r="3" />
                </svg>
              </div>
              <div>
                <h3>Top Ciudades</h3>
                <p>Santa Fe, Buenos Aires, Cordoba, La Plata, San Miguel de Tucuman, Rosario, Mar del Plata.</p>
              </div>
            </div>
            <div className={styles.bannerBlock}>
              <div className={styles.bannerIcon}>
                <svg width="45" height="45" viewBox="0 0 24 24" fill="none" stroke="#ff4b7e" strokeWidth="2">
                  <path d="M18 8h1a4 4 0 010 8h-1" />
                  <path d="M2 8h16v9a4 4 0 01-4 4H6a4 4 0 01-4-4V8z" />
                  <line x1="6" y1="1" x2="6" y2="4" />
                  <line x1="10" y1="1" x2="10" y2="4" />
                  <line x1="14" y1="1" x2="14" y2="4" />
                </svg>
              </div>
              <div>
                <h3>Top Comidas</h3>
                <p>Helados, Pizzas, Hamburguesas, Empanadas, Postres, Sandwiches.</p>
              </div>
            </div>
          </div>
        </section>
      </main>

      <Footer />
    </div>
  );
}
