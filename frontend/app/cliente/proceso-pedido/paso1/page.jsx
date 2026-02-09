'use client';

import { useState } from 'react';
import Image from 'next/image';
import Link from 'next/link';
import Navbar from '../../components/Navbar';
import Footer from './../components/Footer';
import Stepper from '../../components/Stepper';
import ResumenCompra from '../../components/ResumenCompra';
import styles from '../proceso-pedido.module.css';

const initialProducts = [
  {
    id: 1,
    name: 'Doble carne Doble queso + Papas medianas',
    price: 10000,
    qty: 1,
    obs: '',
    img: '/images/producto-burger.jpg',
  },
];

export default function Paso1Page() {
  const [products, setProducts] = useState(initialProducts);

  const updateQty = (id, delta) => {
    setProducts(products.map((p) =>
      p.id === id ? { ...p, qty: Math.max(1, p.qty + delta) } : p
    ));
  };

  const subtotal = products.reduce((s, p) => s + p.price * p.qty, 0);
  const totalItems = products.reduce((s, p) => s + p.qty, 0);

  return (
    <div className={styles.page}>
      <Navbar />

      <main className={styles.main}>
        <div className={styles.header}>
          <Link href="/cliente/carrito" className={styles.backBtn}>
            <svg width="32" height="32" viewBox="0 0 24 24" fill="none">
              <circle cx="12" cy="12" r="11" fill="#fef0f2" stroke="#e84c6a" strokeWidth="1.5" />
              <path d="M14 8l-4 4 4 4" stroke="#e84c6a" strokeWidth="2" />
            </svg>
          </Link>
          <h1 className={styles.title}>PROGRESO DE PEDIDO</h1>
        </div>

        <Stepper activeStep={1} />

        <div className={styles.contentRow}>
          <div className={styles.contentLeft}>
            <h2 className={styles.vendorTitle}>Productos de Burger King Obelisco</h2>
            {products.map((item) => (
              <div key={item.id} className={styles.productItem}>
                <div className={styles.productItemImg}>
                  <Image src={item.img || "/placeholder.svg"} alt={item.name} width={60} height={60} className={styles.productItemImage} />
                </div>
                <div className={styles.productItemInfo}>
                  <div className={styles.productItemTop}>
                    <span className={styles.productItemName}>
                      {item.name}
                      <button className={styles.qtyBtn} style={{ border: 'none', width: 'auto', height: 'auto' }} aria-label="Eliminar">
                        <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="#e84c6a" strokeWidth="2">
                          <polyline points="3 6 5 6 21 6" />
                          <path d="M19 6v14a2 2 0 01-2 2H7a2 2 0 01-2-2V6m3 0V4a2 2 0 012-2h4a2 2 0 012 2v2" />
                        </svg>
                      </button>
                    </span>
                    <span className={styles.productItemPrice}>$ {item.price.toLocaleString()}</span>
                  </div>
                  <p className={styles.productItemObs}>Observaciones: {item.obs || '-'}</p>
                  <div className={styles.qtyControl}>
                    <button className={styles.qtyBtn} onClick={() => updateQty(item.id, -1)}>-</button>
                    <span className={styles.qtyValue}>{item.qty}</span>
                    <button className={styles.qtyBtn} onClick={() => updateQty(item.id, 1)}>+</button>
                  </div>
                </div>
              </div>
            ))}
          </div>
          <ResumenCompra items={totalItems} subtotal={subtotal} />
        </div>

        <div className={styles.continueBtnWrapper}>
          <Link href="/cliente/proceso-pedido/paso2" className={styles.continueBtn}>
            Continuar
          </Link>
        </div>
      </main>

      <Footer />
    </div>
  );
}
