import styles from '../direcciones/direcciones.module.css';
import { useState } from 'react';

const PROVINCIAS = ['Santa Fe', 'Buenos Aires', 'Córdoba', 'Entre Ríos']; 
const LOCALIDADES = ['Santa Fe Capital', 'Humboldt', 'Esperanza', 'Rosario'];

export default function NewAddressModal({ isOpen, onClose, onSuccess }) {
    const [loading, setLoading] = useState(false);
    const [form, setForm] = useState({
    provincia: '',
    localidad: '',
    calle: '',
    numero: '',
    cp: '',
    notas: '',
  });

  if (!isOpen) return null;

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const internalSubmit = (e) => {
    e.preventDefault();
    if (!form.calle || !form.numero) return;
    onSubmit(form);
    // Limpiamos el form para la próxima vez
    setForm({ provincia: '', localidad: '', calle: '', numero: '', cp: '', notas: '' });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      const token = sessionStorage.getItem("token");
      const res = await fetch('/pedidoMs/direcciones', {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(form)
      });

      if (res.ok) {
        onSuccess();
      }
    } catch (error) {
      console.error("Error al guardar dirección:", error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className={styles.modalOverlay} onClick={onClose}>
      <div className={styles.modal} onClick={(e) => e.stopPropagation()}>
        <div className={styles.modalHeader}>
          <h2>Nueva dirección</h2>
          <button className={styles.modalClose} onClick={onClose}>
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <line x1="18" y1="6" x2="6" y2="18" /><line x1="6" y1="6" x2="18" y2="18" />
            </svg>
          </button>
        </div>

        <form className={styles.form} onSubmit={internalSubmit}>
          <div className={styles.formRow}>
            <div className={styles.formGroup}>
              <label className={styles.label}>Provincia</label>
              <select name="provincia" value={form.provincia} onChange={handleChange} className={styles.select}>
                <option value="">Seleccione</option>
                
              </select>
            </div>
            <div className={styles.formGroup}>
              <label className={styles.label}>Localidad</label>
              <select name="localidad" value={form.localidad} onChange={handleChange} className={styles.select}>
                <option value="">Seleccione</option>
                
              </select>
            </div>
          </div>

          <div className={styles.formGroup}>
            <label className={styles.label}>Calle</label>
            <input type="text" name="calle" placeholder="Ej: Rivadavia" value={form.calle} onChange={handleChange} className={styles.input} />
          </div>

          <div className={styles.formRow}>
            <div className={styles.formGroup}>
              <label className={styles.label}>Número</label>
              <input type="text" name="numero" placeholder="1234" value={form.numero} onChange={handleChange} className={styles.input} />
            </div>
            <div className={styles.formGroup}>
              <label className={styles.label}>CP</label>
              <input type="text" name="cp" placeholder="3000" value={form.cp} onChange={handleChange} className={styles.input} />
            </div>
          </div>

          <div className={styles.formGroup}>
            <label className={styles.label}>Notas Adicionales</label>
            <textarea name="notas" placeholder="Piso, depto, etc." value={form.notas} onChange={handleChange} className={styles.textarea} rows={3} />
          </div>

          <button type="submit" className={styles.submitBtn}>Guardar Dirección</button>
        </form>
      </div>
    </div>
  );
}