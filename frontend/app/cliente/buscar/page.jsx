'use client';

import { useState } from 'react';
import Link from 'next/link';
import Image from 'next/image';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';
import styles from './buscar.module.css';

const locales = [
  { name: 'Big Pons - Corrientes', delivery: 'Recibes en 10-30 min', travel: '5-20 min de viaje' },
  { name: 'Mi Barrio Hamburgueseria - Barrio Norte', delivery: 'Listo en 10-30 min', travel: 'Retiro en sucursal' },
  { name: 'Dean Dennys - Av. Corrientes', delivery: 'Recibes en 10-30 min', travel: '5-20 min de viaje' },
  { name: '4090 Burger And Fries - Balvanera', delivery: 'Listo en 10-30 min', travel: 'Retiro en sucursal' },
  { name: 'Mostaza Pellegrini', delivery: 'Recibes en 10-30 min', travel: '5-20 min de viaje' },
  { name: 'Burger King Obelisco', delivery: 'Recibes en 10-30 min', travel: '5-20 min de viaje' },
  { name: 'Hamburguesas Extremas - Florida', delivery: 'Recibes en 10-30 min', travel: '5-20 min de viaje' },
  { name: "McDonald's Lav", delivery: 'Recibes en 10-30 min', travel: '5-20 min de viaje' },
  { name: 'La Birra Bar - San Telmo', delivery: 'Recibes en 10-30 min', travel: '5-20 min de viaje' },
  { name: 'Club De La Birra - Recoleta.', delivery: 'Listo en 10-30 min', travel: 'Retiro en sucursal' },
  { name: 'Angus Brother', delivery: 'Recibes en 10-30 min', travel: '5-20 min de viaje' },
  { name: 'Deltoro Burgers - Irigoyen', delivery: 'Recibes en 10-30 min', travel: '5-20 min de viaje' },
  { name: 'Big Pons - Corrientes', delivery: 'Recibes en 10-30 min', travel: '5-20 min de viaje' },
  { name: 'Mi Barrio Hamburgueseria - Barrio Norte', delivery: 'Listo en 10-30 min', travel: 'Retiro en sucursal' },
  { name: 'Dean Dennys - Av. Corrientes', delivery: 'Recibes en 10-30 min', travel: '5-20 min de viaje' },
  { name: '4090 Burger And Fries - Balvanera', delivery: 'Listo en 10-30 min', travel: 'Retiro en sucursal' },
  { name: 'Mostaza Pellegrini', delivery: 'Recibes en 10-30 min', travel: '5-20 min de viaje' },
  { name: 'Burger King Obelisco', delivery: 'Recibes en 10-30 min', travel: '5-20 min de viaje' },
  { name: 'Hamburguesas Extremas - Florida', delivery: 'Recibes en 10-30 min', travel: '5-20 min de viaje' },
  { name: "McDonald's Lav", delivery: 'Recibes en 10-30 min', travel: '5-20 min de viaje' },
];

const productos = Array.from({ length: 16 }, (_, i) => ({
  id: i,
  name: 'Doble carne Doble queso + Papas medianas',
  price: '$10000',
  store: "Local: McDonald's",
}));

export default function BuscarPage() {
  const [activeTab, setActiveTab] = useState('locales');
  const [searchQuery, setSearchQuery] = useState('');

  return (
    <div className={styles.page}>
      <Navbar />

      <main className={styles.main}>
        <div className={styles.header}>
          <Link href="/cliente" className={styles.backBtn}>
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#e84c6a" strokeWidth="2">
              <circle cx="12" cy="12" r="10" fill="#fef0f2" stroke="#e84c6a" />
              <path d="M14 8l-4 4 4 4" stroke="#e84c6a" />
            </svg>
          </Link>
          <h1 className={styles.title}>BUSCAR</h1>
        </div>

        <div className={styles.searchWrapper}>
          <input
            className={styles.searchInput}
            type="text"
            placeholder="Buscar..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
          />
          <button className={styles.searchBtn} aria-label="Buscar">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="white" strokeWidth="2">
              <circle cx="11" cy="11" r="8" />
              <path d="M21 21l-4.35-4.35" />
            </svg>
          </button>
        </div>

        <div className={styles.tabs}>
          <button
            className={`${styles.tab} ${activeTab === 'locales' ? styles.tabActive : ''}`}
            onClick={() => setActiveTab('locales')}
          >
            Locales
          </button>
          <button
            className={`${styles.tab} ${activeTab === 'productos' ? styles.tabActive : ''}`}
            onClick={() => setActiveTab('productos')}
          >
            Productos
          </button>
        </div>

        <h2 className={styles.resultsTitle}>Resultados para tu busqueda</h2>

        {activeTab === 'locales' && (
          <div className={styles.localesGrid}>
            {locales.map((local, i) => (
              <div key={i} className={styles.localCard}>
                <div className={styles.localLogo}>
                  <div className={styles.localLogoInner}>
                    {local.name.charAt(0)}
                  </div>
                </div>
                <div className={styles.localInfo}>
                  <h3 className={styles.localName}>{local.name}</h3>
                  <div className={styles.localMeta}>
                    <span className={styles.localDelivery}>
                      <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="#e84c6a" strokeWidth="2">
                        <circle cx="12" cy="12" r="10" />
                        <polyline points="12 6 12 12 16 14" />
                      </svg>
                      {local.delivery}
                    </span>
                  </div>
                  <div className={styles.localMeta}>
                    <span className={styles.localTravel}>
                      <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="#888" strokeWidth="2">
                        <circle cx="12" cy="12" r="10" />
                        <polyline points="12 6 12 12 16 14" />
                      </svg>
                      {local.travel}
                    </span>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}

        {activeTab === 'productos' && (
          <div className={styles.productosGrid}>
            {productos.map((prod) => (
              <div key={prod.id} className={styles.productCard}>
                <div className={styles.productImg}>
                  <Image
                    src="/images/producto-burger.jpg"
                    alt={prod.name}
                    width={80}
                    height={80}
                    className={styles.productImage}
                  />
                </div>
                <div className={styles.productInfo}>
                  <h3 className={styles.productName}>{prod.name}</h3>
                  <p className={styles.productPrice}>{prod.price}</p>
                  <p className={styles.productStore}>{prod.store}</p>
                </div>
              </div>
            ))}
          </div>
        )}
      </main>

      <Footer />
    </div>
  );
}
