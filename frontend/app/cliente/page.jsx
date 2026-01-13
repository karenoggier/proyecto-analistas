'use client';
import { Search, MapPin, Star, Clock } from 'lucide-react';
import styles from './cliente.module.css';

export default function ClientePage() {
  return (
    <div className={styles.container}>
      {/* Header Sticky */}
      <header className={styles.header}>
        <div className={styles.addressBar}>
          <MapPin size={18} className={styles.iconRed} />
          <span className={styles.address}>Calle Falsa 123</span>
        </div>
        <div className={styles.searchWrapper}>
          <Search className={styles.searchIcon} size={20} />
          <input type="text" placeholder="¿Qué vas a pedir hoy?" className={styles.searchInput} />
        </div>
      </header>

      {/* Categorías */}
      <section className={styles.categories}>
        {['Hamburguesas', 'Pizza', 'Helado', 'Sushi', 'Pollo', 'Bebidas'].map((cat, i) => (
          <div key={i} className={styles.categoryItem}>
            <div className={styles.catCircle}>🍔</div>
            <span>{cat}</span>
          </div>
        ))}
      </section>

      {/* Listado de Restaurantes */}
      <section className={styles.feed}>
        <h2 className={styles.sectionTitle}>Cerca de ti</h2>
        
        {/* Card Restaurante 1 */}
        {[1, 2, 3].map((item) => (
          <div key={item} className={styles.card}>
            <div className={styles.cardImage}>
              <span className={styles.promoBadge}>Envío Gratis</span>
            </div>
            <div className={styles.cardInfo}>
              <div className={styles.cardHeader}>
                <h3>Burger King</h3>
                <span className={styles.rating}><Star size={12} fill="currentColor"/> 4.5</span>
              </div>
              <p className={styles.meta}>Hamburguesas • $$</p>
              <div className={styles.deliveryInfo}>
                <Clock size={14} /> 20-30 min
              </div>
            </div>
          </div>
        ))}
      </section>
    </div>
  );
}