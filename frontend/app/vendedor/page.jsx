'use client';
import { Bell, Store, CheckCircle, XCircle } from 'lucide-react';
import styles from './vendedor.module.css';

export default function VendedorPage() {
  return (
    <div className={styles.container}>
      {/* Header Dashboard */}
      <header className={styles.header}>
        <div className={styles.brand}>
          <Store className={styles.iconBrand} />
          <span>Mi Negocio</span>
        </div>
        <div className={styles.statusToggle}>
          <span className={styles.dot}></span> Abierto
        </div>
      </header>

      {/* Métricas Rápidas */}
      <div className={styles.statsGrid}>
        <div className={styles.statCard}>
          <h3>Pedidos hoy</h3>
          <p>12</p>
        </div>
        <div className={styles.statCard}>
          <h3>Ingresos</h3>
          <p>$45k</p>
        </div>
      </div>

      {/* Lista de Pedidos Entrantes */}
      <main className={styles.ordersSection}>
        <h2 className={styles.sectionTitle}>
          Pedidos Activos <span className={styles.badge}>3</span>
        </h2>

        {/* Card Pedido */}
        {[101, 102].map((id) => (
          <div key={id} className={styles.orderCard}>
            <div className={styles.orderHeader}>
              <span className={styles.orderId}>#{id}</span>
              <span className={styles.timeAgo}>hace 5 min</span>
            </div>
            
            <div className={styles.itemsList}>
              <p>2x Hamburguesa Doble</p>
              <p>1x Coca Cola 1.5L</p>
            </div>
            
            <div className={styles.customerInfo}>
              Cliente: <strong>Karen Archeron</strong>
            </div>
            
            <div className={styles.actions}>
              <button className={styles.btnReject}>
                <XCircle size={18} /> Rechazar
              </button>
              <button className={styles.btnAccept}>
                <CheckCircle size={18} /> Aceptar
              </button>
            </div>
          </div>
        ))}
      </main>
    </div>
  );
}