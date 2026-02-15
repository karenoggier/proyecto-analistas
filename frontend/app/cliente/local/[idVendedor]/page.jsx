'use client';

import { useState, useEffect } from 'react';
import { useParams } from 'next/navigation';
import Image from 'next/image';
import Link from 'next/link';
import Navbar from '../../components/Navbar';
import Footer from '../../components/Footer';
import styles from '../local.module.css';

const allProducts = [
  {
    id: 1,
    name: 'Doble carne Doble queso + Papas medianas',
    description: 'Hamburguesa doble carne junior 100% vacuna, dos fetas de queso cheddar, ketchup, mostaza y cebolla. Acompanado de papas medianas.',
    price: 10000,
    img: '/images/producto-burger.jpg',
    categoria: 'Comida',
    subcategoria: 'Hamburguesa',
  },
  {
    id: 2,
    name: 'Doble carne Doble queso + Papas medianas',
    description: 'Hamburguesa doble carne junior 100% vacuna, dos fetas de queso cheddar, ketchup, mostaza y cebolla. Acompanado de papas medianas.',
    price: 10000,
    img: '/images/producto-burger.jpg',
    categoria: 'Comida',
    subcategoria: 'Hamburguesa',
  },
  {
    id: 3,
    name: 'McPollo + Papas Pequenas',
    description: 'Medallon de pollo frito, mayonesa y lechuga con papas pequenas.',
    price: 7000,
    img: '/images/producto-chicken.jpg',
    categoria: 'Comida',
    subcategoria: 'Hamburguesa',
  },
  {
    id: 4,
    name: 'McPollo + Papas Pequenas',
    description: 'Medallon de pollo frito, mayonesa y lechuga con papas pequenas.',
    price: 7000,
    img: '/images/producto-chicken.jpg',
    categoria: 'Comida',
    subcategoria: 'Hamburguesa',
  },
  {
    id: 5,
    name: 'McFlurry Oreo',
    description: 'Helado de vainilla, galletitas oreo en trozos con salsa de chocolate.',
    price: 4000,
    img: '/images/producto-mcflurry.jpg',
    categoria: 'Comida',
    subcategoria: 'Helado',
  },
  {
    id: 6,
    name: 'McFlurry Oreo',
    description: 'Helado de vainilla, galletitas oreo en trozos con salsa de chocolate.',
    price: 4000,
    img: '/images/producto-mcflurry.jpg',
    categoria: 'Comida',
    subcategoria: 'Helado',
  },
  {
    id: 7,
    name: 'McFlurry Oreo',
    description: 'Helado de vainilla, galletitas oreo en trozos con salsa de chocolate.',
    price: 4000,
    img: '/images/producto-mcflurry.jpg',
    categoria: 'Comida',
    subcategoria: 'Helado',
  },
  {
    id: 8,
    name: 'McFlurry Oreo',
    description: 'Helado de vainilla, galletitas oreo en trozos con salsa de chocolate.',
    price: 4000,
    img: '/images/producto-mcflurry.jpg',
    categoria: 'Comida',
    subcategoria: 'Helado',
  },
  {
    id: 9,
    name: 'McFlurry Oreo',
    description: 'Helado de vainilla, galletitas oreo en trozos con salsa de chocolate.',
    price: 4000,
    img: '/images/producto-mcflurry.jpg',
    categoria: 'Comida',
    subcategoria: 'Helado',
  },
  {
    id: 10,
    name: 'McFlurry Oreo',
    description: 'Helado de vainilla, galletitas oreo en trozos con salsa de chocolate.',
    price: 4000,
    img: '/images/producto-mcflurry.jpg',
    categoria: 'Comida',
    subcategoria: 'Helado',
  },
];

const categorias = ['Comida', 'Bebida'];
const subcategorias = [
  'Hamburguesa', 'Pizza', 'Empanada', 'Milanesa', 'Pasta', 'Parrilla',
  'Ensalada', 'Sushi', 'Helado', 'Postre', 'Celiaco', 'Vegetariano',
  'Gaseosa', 'Agua', 'Cerveza', 'Vino', 'Trago', 'Cafe',
];

export default function LocalPage() {
  const params = useParams();
  const { idVendedor } = params;
  const [vendorProfile, setVendorProfile] = useState(null);
  const [clientProfile, setClientProfile] = useState(null);
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedProduct, setSelectedProduct] = useState(null);
  const [showFilters, setShowFilters] = useState(false);
  const [quantity, setQuantity] = useState(1);
  const [observations, setObservations] = useState('');
  const [cart, setCart] = useState([]);
  const [filterCats, setFilterCats] = useState([]);
  const [filterSubs, setFilterSubs] = useState([]);

  const toggleFilter = (arr, setArr, val) => {
    setArr(arr.includes(val) ? arr.filter((v) => v !== val) : [...arr, val]);
  };

  const clearFilters = () => {
    setFilterCats([]);
    setFilterSubs([]);
  };

  const activeFilterCount = filterCats.length + filterSubs.length;

  const filteredProducts = allProducts.filter((p) => {
    if (filterCats.length > 0 && !filterCats.includes(p.categoria)) return false;
    if (filterSubs.length > 0 && !filterSubs.includes(p.subcategoria)) return false;
    if (searchQuery && !p.name.toLowerCase().includes(searchQuery.toLowerCase())) return false;
    return true;
  });

  const addToCart = () => {
    if (!selectedProduct) return;
    const existing = cart.find((item) => item.id === selectedProduct.id);
    if (existing) {
      setCart(cart.map((item) =>
        item.id === selectedProduct.id
          ? { ...item, qty: item.qty + quantity, obs: observations }
          : item
      ));
    } else {
      setCart([...cart, { ...selectedProduct, qty: quantity, obs: observations }]);
    }
    setSelectedProduct(null);
    setQuantity(1);
    setObservations('');
  };

  const cartSubtotal = cart.reduce((sum, item) => sum + item.price * item.qty, 0);

  useEffect(() => {
      fetchPerfil();
    }, [])

  useEffect(() => {
    if (idVendedor) {
      fetchVendorProfile();
    }
  }, [idVendedor]);

  const fetchVendorProfile = async () => {
    try {
      const res = await fetch(`/catalogoMs/api/vendedores/perfil-publico/${idVendedor}`);
      if (res.ok) {
        const data = await res.json();
        setVendorProfile(data);
      }
    } catch (error) {
      console.error("Error al cargar perfil del vendedor:", error);
    }
  };

  const fetchPerfil = async () => {
      const token = sessionStorage.getItem("token")
      const rol = sessionStorage.getItem("rol")
  
      if (!token || rol !== "CLIENTE") {
        window.location.href = "/login"
        return
      }
        try {
          const headers = {
              'Authorization': `Bearer ${token}`,
              'Content-Type': 'application/json'
          };
  
          const [perfilRes] = await Promise.all([
              fetch('/pedidoMs/clientes/perfil', { method: 'GET', headers }),
          ]);
  
          if (perfilRes.status === 401 || perfilRes.status === 403) {
              sessionStorage.clear(); 
              window.location.href = "/login?expired=true"; 
              return;
          }
  
          if (perfilRes.ok) {
              const dataPerfil = await perfilRes.json();
              setClientProfile(dataPerfil);
          } else {
              console.error("Error al obtener perfil del cliente");
          }
  
        } catch (error) {
          console.error("Error de red:", error);
        } 
  }

  const handleRefreshProfile = () => {
    fetchPerfil();
  };



  return (
    <div className={styles.page}>
      <Navbar profile={clientProfile} onAddressUpdate={handleRefreshProfile}/>

      <main className={styles.main}>
        {/* Store Header */}
        <div className={styles.storeHeader}>
          <Link href="/cliente/buscar" className={styles.backBtn}>
            <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
               <path d="M15.41 7.41L14 6l-6 6 6 6 1.41-1.41L10.83 12z" />
            </svg>
          </Link>
          <div className={styles.storeLogo}>
            {vendorProfile?.logo ? (
              <img src={vendorProfile.logo} alt={vendorProfile.nombreNegocio} className={styles.storeLogoImg} />
            ) : (
              <div className={styles.storeLogoImg} style={{backgroundColor: '#eee', display: 'flex', alignItems: 'center', justifyContent: 'center'}}>
                 <span style={{fontSize: '2rem', color: '#888'}}>{vendorProfile?.nombreNegocio?.charAt(0)}</span>
              </div>
            )}
          </div>
          <div className={styles.storeInfo}>
            <h1 className={styles.storeName}>{vendorProfile?.nombreNegocio || 'Cargando...'}</h1>
            <div className={styles.storeDetails}>
              <p><svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="#e84c6a" strokeWidth="2"><path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0118 0z" /><circle cx="12" cy="10" r="3" /></svg> 
                {vendorProfile?.direccion ? `${vendorProfile.direccion.calle} ${vendorProfile.direccion.numero}, ${vendorProfile.direccion.localidad}` : 'Dirección no disponible'}
              </p>
              <p><svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="#e84c6a" strokeWidth="2"><circle cx="12" cy="12" r="10" /><polyline points="12 6 12 12 16 14" /></svg> 
                Abre: {vendorProfile?.horarioApertura || '--:--'} hs
              </p>
              <p><svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="#e84c6a" strokeWidth="2"><circle cx="12" cy="12" r="10" /><polyline points="12 6 12 12 16 14" /></svg> 
                Cierra: {vendorProfile?.horarioCierre || '--:--'} hs
              </p>
              <p>
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="#e84c6a" strokeWidth="2">
                  <path d="M5 22h14" />
                  <path d="M5 2h14" />
                  <path d="M17 22v-4.172a2 2 0 0 0-.586-1.414L12 12l-4.414 4.414A2 2 0 0 0 7 17.828V22" />
                  <path d="M7 2v4.172a2 2 0 0 0 .586 1.414L12 12l4.414-4.414A2 2 0 0 0 17 6.172V2" />
                </svg> 
                Tiempo estimado de espera: {vendorProfile?.tiempoEstimadoEspera || '--'}
              </p>
              <p>
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="#e84c6a" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                  <path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72 12.84 12.84 0 0 0 .7 2.81 2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45 12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16.92z" />
                </svg> 
                Teléfono: {vendorProfile?.telefono || '--'}
              </p>
              <p><svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="#e84c6a" strokeWidth="2"><rect x="1" y="3" width="15" height="13" /><polygon points="16 8 20 8 23 11 23 16 16 16 16 8" /><circle cx="5.5" cy="18.5" r="2.5" /><circle cx="18.5" cy="18.5" r="2.5" /></svg> 
                {vendorProfile?.realizaEnvios ? 'Realiza envíos a domicilio' : 'Solo retiro en local'}
              </p>
            </div>
          </div>
        </div>

        {/* Search + Filter */}
        <div className={styles.searchRow}>
          <div className={styles.searchWrapper}>
            <input
              className={styles.searchInput}
              type="text"
              placeholder="Buscar producto..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
            />
            <button className={styles.searchBtn}>
              <svg width="16" height="16" viewBox="0 0 24 24" fill="white">
                <path d="M15.5 14h-.79l-.28-.27C15.41 12.59 16 11.11 16 9.5 16 5.91 13.09 3 9.5 3S3 5.91 3 9.5 5.91 16 9.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z" />
              </svg>
            </button>
          </div>
          <button className={styles.filterBtn} onClick={() => setShowFilters(true)}>
            <svg width="16" height="16" viewBox="0 0 24 24" fill="#ff4b7e">
              <path d="M10 18h4v-2h-4v2zM3 6v2h18V6H3zm3 7h12v-2H6v2z" />
            </svg>
          </button>
        </div>

        {/* Content */}
        <div className={styles.content}>
          <div className={styles.productGrid}>
            {filteredProducts.map((product) => (
              <button
                key={product.id}
                className={styles.productCard}
                onClick={() => { setSelectedProduct(product); setQuantity(1); setObservations(''); }}
              >
                <div className={styles.productInfo}>
                  <h3 className={styles.productName}>{product.name}</h3>
                  <p className={styles.productDesc}>{product.description}</p>
                  <p className={styles.productPrice}>$ {product.price.toLocaleString()}</p>
                </div>
                <div className={styles.productImgWrapper}>
                  <Image src={product.img || "/placeholder.svg"} alt={product.name} width={120} height={120} className={styles.productImg} />
                </div>
              </button>
            ))}
          </div>

          {/* Cart Sidebar */}
          {cart.length > 0 && (
            <aside className={styles.cartSidebar}>
              <div className={styles.cartHeader}>
                <h3>Resumen de carrito</h3>
                <Link href="/cliente/carrito" className={styles.cartEditLink}>Editar</Link>
              </div>
              <div className={styles.cartItems}>
                {cart.map((item) => (
                  <div key={item.id} className={styles.cartItem}>
                    <span className={styles.cartItemQty}>{item.qty} x</span>
                    <span className={styles.cartItemName}>{item.name}</span>
                    <span className={styles.cartItemPrice}>$ {(item.price * item.qty).toLocaleString()}</span>
                  </div>
                ))}
              </div>
              <div className={styles.cartTotal}>
                <span>Subtotal</span>
                <span>${cartSubtotal.toLocaleString()}</span>
              </div>
              <Link href="/cliente/carrito" className={styles.cartGoBtn}>
                Ir al carrito
              </Link>
            </aside>
          )}
        </div>
      </main>

      <Footer />

      {/* Product Detail Modal */}
      {selectedProduct && (
        <div className={styles.modalOverlay} onClick={() => setSelectedProduct(null)}>
          <div className={styles.modal} onClick={(e) => e.stopPropagation()}>
            <button className={styles.modalClose} onClick={() => setSelectedProduct(null)}>
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <line x1="18" y1="6" x2="6" y2="18" />
                <line x1="6" y1="6" x2="18" y2="18" />
              </svg>
            </button>

            <div className={styles.modalImgWrapper}>
              <Image src={selectedProduct.img || "/placeholder.svg"} alt={selectedProduct.name} width={200} height={200} className={styles.modalImg} />
            </div>

            <div className={styles.modalBody}>
              <div className={styles.modalTitleRow}>
                <h2 className={styles.modalTitle}>{selectedProduct.name}</h2>
                <span className={styles.modalPrice}>$ {selectedProduct.price.toLocaleString()}</span>
              </div>
              <p className={styles.modalDesc}>{selectedProduct.description}</p>

              <div className={styles.modalField}>
                <label className={styles.modalLabel}>Unidades</label>
                <div className={styles.qtyControl}>
                  <button
                    className={styles.qtyBtn}
                    onClick={() => setQuantity(Math.max(1, quantity - 1))}
                  >-</button>
                  <span className={styles.qtyValue}>{quantity}</span>
                  <button
                    className={styles.qtyBtn}
                    onClick={() => setQuantity(quantity + 1)}
                  >+</button>
                </div>
              </div>

              <div className={styles.modalField}>
                <label className={styles.modalLabel}>Observaciones</label>
                <textarea
                  className={styles.modalTextarea}
                  value={observations}
                  onChange={(e) => setObservations(e.target.value)}
                  rows={3}
                />
              </div>

              <button className={styles.addToCartBtn} onClick={addToCart}>
                Agregar al carrito
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Filters Modal */}
      {showFilters && (
        <div className={styles.modalOverlay} onClick={() => setShowFilters(false)}>
          <div className={styles.filtersModal} onClick={(e) => e.stopPropagation()}>
            <div className={styles.filtersHeader}>
              <h2>Filtros ({activeFilterCount})</h2>
              <button className={styles.modalClose} onClick={() => setShowFilters(false)}>
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                  <line x1="18" y1="6" x2="6" y2="18" />
                  <line x1="6" y1="6" x2="18" y2="18" />
                </svg>
              </button>
            </div>

            <div className={styles.filterSection}>
              <div className={styles.filterSectionHeader}>
                <h3>Categorias</h3>
              </div>
              <div className={styles.filterCheckboxes}>
                {categorias.map((cat) => (
                  <label key={cat} className={styles.checkboxLabel}>
                    <input
                      type="checkbox"
                      checked={filterCats.includes(cat)}
                      onChange={() => toggleFilter(filterCats, setFilterCats, cat)}
                      className={styles.checkbox}
                    />
                    {cat}
                  </label>
                ))}
              </div>
            </div>

            <div className={styles.filterSection}>
              <div className={styles.filterSectionHeader}>
                <h3>Subcategorias</h3>
              </div>
              <div className={styles.filterSubGrid}>
                {subcategorias.map((sub) => (
                  <label key={sub} className={styles.checkboxLabel}>
                    <input
                      type="checkbox"
                      checked={filterSubs.includes(sub)}
                      onChange={() => toggleFilter(filterSubs, setFilterSubs, sub)}
                      className={styles.checkbox}
                    />
                    {sub}
                  </label>
                ))}
              </div>
            </div>

            <div className={styles.filterActions}>
              <button className={styles.filterApplyBtn} onClick={() => setShowFilters(false)}>Aplicar</button>
              <button className={styles.filterClearBtn} onClick={clearFilters}>Limpiar filtros</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
