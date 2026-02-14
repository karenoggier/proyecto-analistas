import styles from './Navbar.module.css';
import { useState, useEffect } from 'react';

export default function AddressModal({ isOpen, onClose, direcciones, onOpenNewAddress }) {
  const [selectedId, setSelectedId] = useState(null);

  useEffect(() => {
    if (isOpen) {
      const savedId = sessionStorage.getItem("selectedAddressId");
      if (savedId) {
        const existe = direcciones?.some(d => d.id === savedId);
        setSelectedId(existe ? savedId : (direcciones?.[0]?.id || null));
      } else if (direcciones && direcciones.length > 0) {
        setSelectedId(direcciones[0].id);
      }
    }
  }, [isOpen, direcciones]);

  const handleSelectAddress = (id) => {
    setSelectedId(id);
    sessionStorage.setItem("selectedAddressId", id);

    window.dispatchEvent(new Event("storage"));
    window.dispatchEvent(new Event("addressChanged"));
    
    //setTimeout(() => onClose(), 300);
  };
  
  
  if (!isOpen) return null;

  return (
    <div className={styles.modalOverlay} onClick={onClose}>
      <div className={styles.modalContent} onClick={(e) => e.stopPropagation()}>
        <div className={styles.popoverHeader}>
          <h3>Direcciones</h3>
          <button className={styles.popoverClose} onClick={onClose}>
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <path d="M18 6L6 18M6 6l12 12" />
            </svg>
          </button>
        </div>
        
        <p className={styles.popoverSubtext}>
          {direcciones && direcciones.length > 0 ? "Elija una dirección..." : "No tienes direcciones registradas"}
        </p>

        <div className={styles.addressContainer}>
          <div className={styles.addressList}>
            {direcciones && direcciones.length > 0 ? (
              direcciones.map((dir, index) => (
                <label key={dir.id || index} className={styles.addressItem}>
                  <input 
                    type="radio" 
                    name="address" 
                    checked={selectedId === dir.id}
                    onChange={() => handleSelectAddress(dir.id)}
                    className={styles.addressRadio} 
                  />
                  <div className={styles.addressText}>
                    <strong>{dir.calle} {dir.numero}</strong>
                    <span>CP: {dir.codigoPostal} - {dir.localidad}, {dir.provincia}</span>
                  </div>
                </label>
              ))
            ) : (
              <div className={styles.emptyAddressState}>
                <p></p>
              </div>
            )}
          </div>

          <button 
            type="button"
            className={styles.addAddressLink} 
            onClick={() => {
              onClose(); 
              onOpenNewAddress();
            }}
          >
            <span className={styles.plusIcon}>+</span> Agregar dirección
          </button>
        </div>
      </div>
    </div>
  );
}