'use client';

import { useState } from 'react';
import Image from 'next/image';
import Link from 'next/link';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';
import styles from './carrito.module.css';

const initialCarts = [
  {
    vendor: 'Burger King Obelisco',
    items: [
      {
        id: 1,
        name: 'Doble carne Doble queso + Papas medianas',
        price: 10000,
        qty: 1,
        obs: '',
        img: '/images/producto-burger.jpg',
      },
    ],
  },
  {
    vendor: "McDonald's",
    items: [
      {
        id: 2,
        name: 'Doble carne Doble queso + Papas medianas',
        price: 10000,
        qty: 1,
        obs: '',
        img: '/images/producto-burger.jpg',
      },
      {
        id: 3,
        name: 'McFlurry Oreo',
        price: 4000,
        qty: 1,
        obs: '',
        img: '/images/producto-mcflurry.jpg',
      },
    ],
  },
];

const ENVIO = 1200;
const SERVICIO = 500;

export default function CarritoPage() {
  const [carts, setCarts] = useState(initialCarts);

  const updateQty = (vendorIdx, itemId, delta) => {
    setCarts(carts.map((cart, ci) => {
      if (ci !== vendorIdx) return cart;
      return {
        ...cart,
        items: cart.items.map((item) => {
          if (item.id !== itemId) return item;
          const newQty = Math.max(1, item.qty + delta);
          return { ...item, qty: newQty };
        }),
      };
    }));
  };

  const removeItem = (vendorIdx, itemId) => {
    setCarts(carts.map((cart, ci) => {
      if (ci !== vendorIdx) return cart;
      return {
        ...cart,
        items: cart.items.filter((item) => item.id !== itemId),
      };
    }).filter((cart) => cart.items.length > 0));
  };

  return (
    <div className={styles.page}>
      <Navbar />

      <main className={styles.main}>
        <div className={styles.header}>
          <Link href="/cliente" className={styles.backBtn}>
            <svg width="32" height="32" viewBox="0 0 24 24" fill="none">
              <circle cx="12" cy="12" r="11" fill="#fef0f2" stroke="#e84c6a" strokeWidth="1.5" />
              <path d="M14 8l-4 4 4 4" stroke="#e84c6a" strokeWidth="2" />
            </svg>
          </Link>
          <h1 className={styles.title}>MIS CARRITOS</h1>
        </div>

        <div className={styles.cartList}>
          {carts.map((cart, vendorIdx) => {
            const subtotal = cart.items.reduce((s, item) => s + item.price * item.qty, 0);
            const total = subtotal + ENVIO + SERVICIO;
            const totalItems = cart.items.reduce((s, item) => s + item.qty, 0);

            return (
              <div key={vendorIdx} className={styles.cartGroup}>
                <div className={styles.cartGroupInner}>
                  <div className={styles.cartLeft}>
                    <div className={styles.vendorHeader}>
                      <div className={styles.vendorDot} />
                      <h2 className={styles.vendorName}>Productos de {cart.vendor}</h2>
                    </div>

                    <div className={styles.itemList}>
                      {cart.items.map((item) => (
                        <div key={item.id} className={styles.cartItem}>
                          <div className={styles.itemImg}>
                            <Image src={item.img || "/placeholder.svg"} alt={item.name} width={60} height={60} className={styles.itemImage} />
                          </div>
                          <div className={styles.itemInfo}>
                            <div className={styles.itemTop}>
                              <span className={styles.itemName}>
                                {item.name}
                                <button
                                  className={styles.deleteItemBtn}
                                  onClick={() => removeItem(vendorIdx, item.id)}
                                  aria-label="Eliminar"
                                >
                                  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="#e84c6a" strokeWidth="2">
                                    <polyline points="3 6 5 6 21 6" />
                                    <path d="M19 6v14a2 2 0 01-2 2H7a2 2 0 01-2-2V6m3 0V4a2 2 0 012-2h4a2 2 0 012 2v2" />
                                  </svg>
                                </button>
                              </span>
                              <span className={styles.itemPrice}>$ {item.price.toLocaleString()}</span>
                            </div>
                            <p className={styles.itemObs}>Observaciones: {item.obs || '-'}</p>
                            <div className={styles.qtyControl}>
                              <button className={styles.qtyBtn} onClick={() => updateQty(vendorIdx, item.id, -1)}>-</button>
                              <span className={styles.qtyValue}>{item.qty}</span>
                              <button className={styles.qtyBtn} onClick={() => updateQty(vendorIdx, item.id, 1)}>+</button>
                            </div>
                          </div>
                        </div>
                      ))}
                    </div>
                  </div>

                  <div className={styles.cartRight}>
                    <div className={styles.summaryCard}>
                      <h3 className={styles.summaryTitle}>Resumen de compra</h3>
                      <div className={styles.summaryRow}>
                        <span>Productos ({totalItems})</span>
                        <span>${subtotal.toLocaleString()}</span>
                      </div>
                      <div className={styles.summaryRow}>
                        <span>Costo de envio</span>
                        <span>${ENVIO.toLocaleString()}</span>
                      </div>
                      <div className={styles.summaryRow}>
                        <span>Tarifa de servicio</span>
                        <span>${SERVICIO.toLocaleString()}</span>
                      </div>
                      <div className={styles.summaryTotal}>
                        <span>Total</span>
                        <span>${total.toLocaleString()}</span>
                      </div>
                      <Link href="/cliente/proceso-pedido/paso1" className={styles.continueBtn}>
                        Continuar
                      </Link>
                    </div>
                  </div>
                </div>
              </div>
            );
          })}
        </div>
      </main>

      <Footer />
    </div>
  );
}