'use client';

import Image from 'next/image';
import Link from 'next/link';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';
import styles from './detalle-pedido.module.css';

export default function DetallePedidoPage() {
  return (
    <div className={styles.page}>
      <Navbar />

      <main className={styles.main}>
        <div className={styles.header}>
          <Link href="/cliente/mis-pedidos" className={styles.backBtn}>
            <svg width="32" height="32" viewBox="0 0 24 24" fill="none">
              <circle cx="12" cy="12" r="11" fill="#fef0f2" stroke="#e84c6a" strokeWidth="1.5" />
              <path d="M14 8l-4 4 4 4" stroke="#e84c6a" strokeWidth="2" />
            </svg>
          </Link>
          <h1 className={styles.title}>DETALLE DEL PEDIDO</h1>
        </div>

        <div className={styles.contentRow}>
          {/* Left Column */}
          <div className={styles.leftCol}>
            {/* Order Info */}
            <div className={styles.card}>
              <h3 className={styles.cardTitle}>Informacion del pedido</h3>
              <div className={styles.infoRow}>
                <span className={styles.infoLabel}>Pedido N:</span>
                <span className={styles.infoValue}>#12345</span>
              </div>
              <div className={styles.infoRow}>
                <span className={styles.infoLabel}>Fecha:</span>
                <span className={styles.infoValue}>05/02/2026</span>
              </div>
              <div className={styles.infoRow}>
                <span className={styles.infoLabel}>Local:</span>
                <span className={styles.infoValue}>Burger King Obelisco</span>
              </div>
              <div className={styles.infoRow}>
                <span className={styles.infoLabel}>Metodo de pago:</span>
                <span className={styles.infoValue}>Mercado Pago</span>
              </div>
            </div>

            {/* Shipping Info */}
            <div className={styles.card}>
              <h3 className={styles.cardTitle}>Informacion del envio</h3>
              <div className={styles.infoRow}>
                <span className={styles.infoLabel}>Direccion:</span>
                <span className={styles.infoValue}>Santos Vianni 1032</span>
              </div>
              <div className={styles.infoRow}>
                <span className={styles.infoLabel}>Localidad:</span>
                <span className={styles.infoValue}>Humboldt, Santa Fe</span>
              </div>
              <div className={styles.infoRow}>
                <span className={styles.infoLabel}>CP:</span>
                <span className={styles.infoValue}>3081</span>
              </div>
            </div>

            {/* Products */}
            <div className={styles.card}>
              <h3 className={styles.cardTitle}>Productos</h3>
              <div className={styles.productItem}>
                <div className={styles.productImg}>
                  <Image src="/images/producto-burger.jpg" alt="Burger" width={50} height={50} className={styles.productImage} />
                </div>
                <div className={styles.productInfo}>
                  <span className={styles.productName}>Doble carne Doble queso + Papas medianas</span>
                  <span className={styles.productQty}>x1</span>
                </div>
                <span className={styles.productPrice}>$10.000</span>
              </div>
            </div>

            {/* Order Status */}
            <div className={styles.card}>
              <h3 className={styles.cardTitle}>Estado del pedido</h3>
              <div className={styles.statusTimeline}>
                <div className={`${styles.statusStep} ${styles.statusStepDone}`}>
                  <div className={styles.statusCircle}>
                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="white" strokeWidth="3">
                      <polyline points="20 6 9 17 4 12" />
                    </svg>
                  </div>
                  <span>Realizado</span>
                </div>
                <div className={`${styles.statusLine} ${styles.statusLineDone}`} />
                <div className={`${styles.statusStep} ${styles.statusStepDone}`}>
                  <div className={styles.statusCircle}>
                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="white" strokeWidth="3">
                      <polyline points="20 6 9 17 4 12" />
                    </svg>
                  </div>
                  <span>En preparacion</span>
                </div>
                <div className={`${styles.statusLine} ${styles.statusLineDone}`} />
                <div className={`${styles.statusStep} ${styles.statusStepDone}`}>
                  <div className={styles.statusCircle}>
                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="white" strokeWidth="3">
                      <polyline points="20 6 9 17 4 12" />
                    </svg>
                  </div>
                  <span>En camino</span>
                </div>
                <div className={`${styles.statusLine} ${styles.statusLineDone}`} />
                <div className={`${styles.statusStep} ${styles.statusStepDone}`}>
                  <div className={styles.statusCircle}>
                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="white" strokeWidth="3">
                      <polyline points="20 6 9 17 4 12" />
                    </svg>
                  </div>
                  <span>Entregado</span>
                </div>
              </div>
            </div>
          </div>

          {/* Right Column - Summary */}
          <div className={styles.rightCol}>
            <div className={styles.summaryCard}>
              <h3 className={styles.summaryTitle}>Resumen de compra</h3>
              <div className={styles.summaryRow}>
                <span>Productos (1)</span>
                <span>$10.000</span>
              </div>
              <div className={styles.summaryRow}>
                <span>Costo de envio</span>
                <span>$1.200</span>
              </div>
              <div className={styles.summaryRow}>
                <span>Tarifa de servicio</span>
                <span>$500</span>
              </div>
              <div className={styles.summaryTotal}>
                <span>Total</span>
                <span>$11.700</span>
              </div>
              <button className={styles.cancelBtn}>
                Cancelar pedido
              </button>
            </div>
          </div>
        </div>
      </main>

      <Footer />
    </div>
  );
}
