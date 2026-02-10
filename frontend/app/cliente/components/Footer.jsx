import styles from './Footer.module.css';

export default function Footer() {
  return (
    <footer className={styles.footer}>
        <div className={`${styles.container} ${styles.footerInner}`}>
          <p className={styles.footerText}>PediloYa © 2026. Todos los derechos reservados.</p>
        </div>
    </footer>
  );
}
