import styles from '../direcciones/direcciones.module.css';
import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { useAppDialog } from '../../../components/ui/app-dialog';

export default function NewAddressModal({ isOpen, onClose, onSuccess }) {
  const { showAlert } = useAppDialog();
  const router = useRouter();
  const [loading, setLoading] = useState(false);
  
  const [provincias, setProvincias] = useState([])
  const [localidades, setLocalidades] = useState([])
  const [loadingLocalidades, setLoadingLocalidades] = useState(false)

  const [errorMsg, setErrorMsg] = useState(""); 
  const [fieldErrors, setFieldErrors] = useState({});
  
  const [form, setForm] = useState({
    provincia: "",
    localidad: "",
    calle: "",
    numero: "",
    codigoPostal: "",
    observaciones: ""
  });

  // ========= EFFECTS (CARGA DE DATOS) =========
  useEffect(() => {
        if (!isOpen) return;

        const cargarProvincias = async () => {
            try {
                const resProv = await fetch('/usuariosMs/ubicacion/provincias');
                const listaProvincias = await resProv.json();
                setProvincias(listaProvincias);
            } catch (error) {
                console.error("Error cargando provincias:", error);
            }
        };
        cargarProvincias();
    }, [isOpen]);


  if (!isOpen) return null;

  // ========= MANEJADORES DE EVENTOS =========
    const handleChange = async (e) => {
        const { name, value } = e.target;

        setForm(prev => ({
            ...prev,
            [name]: value,
            ...(name === 'provincia' ? { localidad: "" } : {}) 
        }));

        if (name === 'provincia' && value) {
            setLoadingLocalidades(true);
            try {
                const res = await fetch(`/usuariosMs/ubicacion/localidades/${value}`);
                if (res.ok) setLocalidades(await res.json());
                else setLocalidades([]);
            } catch (err) {
                setLocalidades([]);
            } finally {
                setLoadingLocalidades(false);
            }
        }
    };

    const validate = () => {
        let errors = {};
        if (!form.provincia) errors.provincia = "Seleccione una provincia";
        if (!form.localidad) errors.localidad = "Seleccione una localidad";
        if (!form.calle.trim()) errors.calle = "La calle es obligatoria";
        if (!form.numero.trim()) errors.numero = "El número es obligatorio";
        if (!form.codigoPostal.trim()) errors.codigoPostal = "El CP es obligatorio";
        setFieldErrors(errors);
        return Object.keys(errors).length === 0;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!validate()) return;

        setLoading(true);
        setErrorMsg("");
        setFieldErrors({});

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
              await showAlert({
                title: "Operación exitosa",
                description: "¡Dirección registrada correctamente!",
              })

              setForm({
                  provincia: "",
                  localidad: "",
                  calle: "",
                  numero: "",
                  codigoPostal: "",
                  observaciones: ""
              });

              setFieldErrors({});

              router.refresh();
              onSuccess(); 
              
            } else if (res.status === 400) {
              const errorData = await res.json();
              
              if (errorData.mensaje) {
                  setErrorMsg(errorData.mensaje);
              } 
              else {
                  setFieldErrors(errorData); 
              }
          } else {
              setErrorMsg(`Error ${res.status}: El servidor no pudo procesar la solicitud.`);
          }
        } catch (error) {
            console.error("Error al guardar:", error);
            setErrorMsg("Error al guardar");
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

        <form className={styles.form} onSubmit={handleSubmit}>
          <div className={styles.formRow}>
            <div className={styles.formGroup}>
              <label className={styles.label}>Provincia</label>
              <select 
                name="provincia" 
                value={form.provincia} 
                onChange={handleChange} 
                className={styles.select}
              >
                <option value="">Seleccione</option>
                {provincias.map(p => <option key={p.id} value={p.id}>{p.nombre}</option>)}
              </select>
              {fieldErrors.provincia && <p className={styles.errorMsg}>{fieldErrors.provincia}</p>}
            </div>
            <div className={styles.formGroup}>
              <label className={styles.label}>Localidad</label>
              <select 
                name="localidad" 
                value={form.localidad} 
                onChange={handleChange} 
                className={styles.select}
              >
                <option value="">{loadingLocalidades ? "Cargando..." : "Seleccione"}</option>
                {localidades.map(l => <option key={l.id} value={l.id}>{l.nombre}</option>)}
              </select>
              {fieldErrors.localidad && <p className={styles.errorMsg}>{fieldErrors.localidad}</p>}
            </div>
          </div>

          <div className={styles.formGroup}>
            <label className={styles.label}>Calle</label>
            <input 
              type="text" 
              name="calle" 
              placeholder="Ej: Rivadavia" 
              value={form.calle} 
              onChange={handleChange} 
              className={styles.input} 
            />
            {fieldErrors.calle && <p className={styles.errorMsg}>{fieldErrors.calle}</p>}
          </div>

          <div className={styles.formRow}>
            <div className={styles.formGroup}>
              <label className={styles.label}>Número</label>
              <input 
                type="text" 
                name="numero" 
                placeholder="1234" 
                value={form.numero} 
                onChange={handleChange} 
                className={styles.input} 
              />
              {fieldErrors.numero && <p className={styles.errorMsg}>{fieldErrors.numero}</p>}
            </div>

            <div className={styles.formGroup}>
              <label className={styles.label}>CP</label>
              <input 
                type="text" 
                name="codigoPostal" 
                placeholder="3000" 
                value={form.codigoPostal} 
                onChange={handleChange} 
                className={styles.input} 
              />
              {fieldErrors.codigoPostal && <p className={styles.errorMsg}>{fieldErrors.codigoPostal}</p>}
            </div>
          </div>

          <div className={styles.formGroup}>
            <label className={styles.label}>Notas Adicionales</label>
            <textarea 
              name="observaciones" 
              placeholder="Piso, depto, etc." 
              value={form.observaciones} 
              onChange={handleChange} 
              className={styles.textarea} 
              rows={3} 
            />
            {fieldErrors.observaciones && <p className={styles.errorMsg}>{fieldErrors.observaciones}</p>}
          </div>

          {errorMsg && (
            <div className={styles.errorMessage}>
              <span>{errorMsg}</span>
            </div>
          )}

          <button type="submit" className={styles.submitBtn} disabled={loading}>
            {loading ? "Guardando..." : "Guardar Dirección"}
          </button>
        </form>
      </div>
    </div>
  );
}