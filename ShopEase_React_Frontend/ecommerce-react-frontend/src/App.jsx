import { useEffect, useMemo, useState } from 'react'
import axios from 'axios'
import { ShoppingBag, Package, Users, ShoppingCart, ClipboardList, RefreshCw, Plus, Trash2, CheckCircle2, AlertCircle } from 'lucide-react'

const API = 'http://localhost:8886/api'
const api = axios.create({ baseURL: API, headers: { 'Content-Type': 'application/json' } })

export default function App() {
  const [tab, setTab] = useState('products')
  const [products, setProducts] = useState([])
  const [customers, setCustomers] = useState([])
  const [cart, setCart] = useState([])
  const [orders, setOrders] = useState([])
  const [customerId, setCustomerId] = useState('')
  const [quantity, setQuantity] = useState(1)
  const [selectedProduct, setSelectedProduct] = useState('')
  const [notice, setNotice] = useState({ type: '', text: '' })
  const [busy, setBusy] = useState(false)
  const [productForm, setProductForm] = useState({ name: '', description: '', price: '', stock: '' })
  const [customerForm, setCustomerForm] = useState({ name: '', email: '', phone: '' })

  const cartTotal = useMemo(() => cart.reduce((sum, item) => sum + Number(item.totalAmount || 0), 0), [cart])

  function message(type, text) {
    setNotice({ type, text })
    window.setTimeout(() => setNotice({ type: '', text: '' }), 4500)
  }

  async function loadProducts() {
    const { data } = await api.get('/products')
    setProducts(Array.isArray(data) ? data : [])
  }
  async function loadCustomers() {
    const { data } = await api.get('/customers')
    setCustomers(Array.isArray(data) ? data : [])
  }
  async function loadCart(id = customerId) {
    if (!id) return setCart([])
    const { data } = await api.get(`/orders/cart/${id}`)
    setCart(Array.isArray(data) ? data : [])
  }
  async function loadOrders(id = customerId) {
    if (!id) return setOrders([])
    const { data } = await api.get(`/orders/customer/${id}`)
    setOrders(Array.isArray(data) ? data : [])
  }

  async function refreshAll() {
    setBusy(true)
    try {
      await Promise.all([loadProducts(), loadCustomers()])
      if (customerId) await Promise.all([loadCart(), loadOrders()])
      message('success', 'Data refreshed from Spring Boot.')
    } catch (e) {
      message('error', errorText(e))
    } finally { setBusy(false) }
  }

  useEffect(() => {
    refreshInitial()
  }, [])

  async function refreshInitial() {
    try { await Promise.all([loadProducts(), loadCustomers()]) }
    catch (e) { message('error', `Cannot connect to backend. ${errorText(e)}`) }
  }

  function errorText(e) {
    return e?.response?.data?.message || e?.response?.data?.error || e?.message || 'Something went wrong'
  }

  async function createProduct(e) {
    e.preventDefault()
    try {
      await api.post('/products', { ...productForm, price: Number(productForm.price), stock: Number(productForm.stock) })
      setProductForm({ name: '', description: '', price: '', stock: '' })
      await loadProducts()
      message('success', 'Product added successfully.')
    } catch (e) { message('error', errorText(e)) }
  }

  async function createCustomer(e) {
    e.preventDefault()
    try {
      await api.post('/customers', customerForm)
      setCustomerForm({ name: '', email: '', phone: '' })
      await loadCustomers()
      message('success', 'Customer registered successfully.')
    } catch (e) { message('error', errorText(e)) }
  }

  async function addToCart(e) {
    e.preventDefault()
    if (!customerId || !selectedProduct) return message('error', 'Select a customer and a product first.')
    try {
      await api.post('/orders/cart', { customerId: Number(customerId), productId: Number(selectedProduct), quantity: Number(quantity) })
      await loadCart()
      message('success', 'Product added to cart.')
      setTab('cart')
    } catch (e) { message('error', errorText(e)) }
  }

  async function checkout(orderId) {
    try {
      await api.post(`/orders/checkout/${orderId}`)
      await Promise.all([loadCart(), loadOrders(), loadProducts()])
      message('success', 'Order placed! Product stock has been updated.')
    } catch (e) { message('error', errorText(e)) }
  }

  async function deleteCartItem(orderId) {
    try {
      await api.delete(`/orders/${orderId}`)
      await loadCart()
      message('success', 'Cart item removed.')
    } catch (e) { message('error', errorText(e)) }
  }

  async function selectCustomer(id) {
    setCustomerId(id)
    if (!id) { setCart([]); setOrders([]); return }
    try { await Promise.all([loadCart(id), loadOrders(id)]) }
    catch (e) { message('error', errorText(e)) }
  }

  const nav = [
    ['products', 'Products', Package],
    ['customers', 'Customers', Users],
    ['cart', 'Shopping cart', ShoppingCart],
    ['orders', 'My orders', ClipboardList],
  ]

  return <div className="app-shell">
    <aside className="sidebar">
      <div className="brand"><span className="brand-icon"><ShoppingBag size={21}/></span><span>ShopEase<small>SPRING BOOT STORE</small></span></div>
      <div className="nav-label">WORKSPACE</div>
      {nav.map(([key, label, Icon]) => <button key={key} className={`nav-item ${tab === key ? 'active' : ''}`} onClick={() => setTab(key)}><Icon size={18}/>{label}</button>)}
      <div className="sidebar-bottom"><span className="live-dot"/> Backend API <b>localhost:8143</b></div>
    </aside>

    <main className="main">
      <header className="topbar">
        <div><div className="eyebrow">E-COMMERCE MANAGEMENT</div><h1>{nav.find(n => n[0] === tab)?.[1]}</h1></div>
        <div className="top-actions">
          <select value={customerId} onChange={e => selectCustomer(e.target.value)} aria-label="Choose customer">
            <option value="">Select customer</option>
            {customers.map(c => <option key={c.id} value={c.id}>{c.name} (#{c.id})</option>)}
          </select>
          <button className="icon-button" onClick={refreshAll} title="Refresh"><RefreshCw size={17}/></button>
        </div>
      </header>

      {notice.text && <div className={`notice ${notice.type}`}><span>{notice.type === 'success' ? <CheckCircle2 size={18}/> : <AlertCircle size={18}/>}</span>{notice.text}</div>}

      {tab === 'products' && <section>
        <div className="section-heading"><div><h2>Product catalog</h2><p>Browse products stored in your MySQL database.</p></div><span className="count-pill">{products.length} products</span></div>
        <div className="product-grid">
          {products.map(p => <article className="product-card" key={p.id}>
            <div className="product-art"><Package size={34}/><span>PRODUCT</span></div>
            <div className="product-info"><div className="product-title-row"><h3>{p.name}</h3><span className="stock">{p.stock} in stock</span></div>
              <p>{p.description || 'No description provided.'}</p><div className="price-row"><strong>₹{Number(p.price || 0).toLocaleString('en-IN')}</strong><button className="small-primary" onClick={() => { setSelectedProduct(String(p.id)); setTab('cart') }}>Add to cart <Plus size={14}/></button></div>
            </div>
          </article>)}
          {!products.length && <Empty title="No products yet" text="Add your first product using the form below."/>}
        </div>
        <div className="panel form-panel"><div className="panel-title"><div><h2>Add a product</h2><p>Create a new item in the catalog.</p></div></div>
          <form className="form-grid" onSubmit={createProduct}>
            <Field label="Product name"><input required value={productForm.name} onChange={e => setProductForm({...productForm, name:e.target.value})} placeholder="e.g. Wireless headphones"/></Field>
            <Field label="Price (₹)"><input required min="0" type="number" step="0.01" value={productForm.price} onChange={e => setProductForm({...productForm, price:e.target.value})} placeholder="2499"/></Field>
            <Field label="Available stock"><input required min="0" type="number" value={productForm.stock} onChange={e => setProductForm({...productForm, stock:e.target.value})} placeholder="20"/></Field>
            <Field label="Description" wide><input value={productForm.description} onChange={e => setProductForm({...productForm, description:e.target.value})} placeholder="Short product description"/></Field>
            <div className="form-actions"><button className="primary" type="submit"><Plus size={16}/> Save product</button></div>
          </form>
        </div>
      </section>}

      {tab === 'customers' && <section>
        <div className="section-heading"><div><h2>Customer directory</h2><p>Register customers and select one to manage their cart.</p></div><span className="count-pill">{customers.length} customers</span></div>
        <div className="panel"><div className="panel-title"><div><h2>Registered customers</h2><p>Customer records retrieved from the backend.</p></div></div>
          <div className="table-wrap"><table><thead><tr><th>ID</th><th>NAME</th><th>EMAIL</th><th>PHONE</th><th>ACTION</th></tr></thead><tbody>
            {customers.map(c => <tr key={c.id}><td>#{c.id}</td><td className="strong-cell">{c.name}</td><td>{c.email}</td><td>{c.phone || '—'}</td><td><button className="text-action" onClick={() => selectCustomer(String(c.id))}>Select customer</button></td></tr>)}
            {!customers.length && <tr><td colSpan="5" className="empty-cell">No customers found. Register one below.</td></tr>}
          </tbody></table></div>
        </div>
        <div className="panel form-panel"><div className="panel-title"><div><h2>Register a customer</h2><p>Customer details are saved through POST /api/customers.</p></div></div>
          <form className="form-grid" onSubmit={createCustomer}>
            <Field label="Full name"><input required value={customerForm.name} onChange={e => setCustomerForm({...customerForm, name:e.target.value})} placeholder="Customer name"/></Field>
            <Field label="Email address"><input required type="email" value={customerForm.email} onChange={e => setCustomerForm({...customerForm, email:e.target.value})} placeholder="name@example.com"/></Field>
            <Field label="Phone number"><input value={customerForm.phone} onChange={e => setCustomerForm({...customerForm, phone:e.target.value})} placeholder="9876543210"/></Field>
            <div className="form-actions"><button className="primary" type="submit"><Plus size={16}/> Register customer</button></div>
          </form>
        </div>
      </section>}

      {tab === 'cart' && <section>
        <div className="section-heading"><div><h2>Shopping cart</h2><p>Add products for the selected customer and proceed to checkout.</p></div></div>
        <div className="panel form-panel"><div className="panel-title"><div><h2>Add item to cart</h2><p>Choose a customer from the top-right dropdown first.</p></div></div>
          <form className="form-grid" onSubmit={addToCart}>
            <Field label="Product"><select required value={selectedProduct} onChange={e => setSelectedProduct(e.target.value)}><option value="">Choose product</option>{products.map(p => <option key={p.id} value={p.id}>{p.name} — ₹{p.price} (stock: {p.stock})</option>)}</select></Field>
            <Field label="Quantity"><input required type="number" min="1" value={quantity} onChange={e => setQuantity(e.target.value)}/></Field>
            <div className="form-actions"><button className="primary" type="submit" disabled={!customerId}><Plus size={16}/> Add to cart</button></div>
          </form>
        </div>
        <div className="panel"><div className="panel-title"><div><h2>Cart items</h2><p>{customerId ? `Items for customer #${customerId}` : 'Select a customer to view their cart.'}</p></div><span className="count-pill">{cart.length} items</span></div>
          <div className="table-wrap"><table><thead><tr><th>ORDER ID</th><th>PRODUCT</th><th>QUANTITY</th><th>AMOUNT</th><th>STATUS</th><th>ACTION</th></tr></thead><tbody>
            {cart.map(o => <tr key={o.id}><td>#{o.id}</td><td className="strong-cell">{o.product?.name || `Product #${o.product?.id || ''}`}</td><td>{o.quantity}</td><td>₹{Number(o.totalAmount || 0).toLocaleString('en-IN')}</td><td><span className="status cart-status">{o.status}</span></td><td className="actions-cell"><button className="small-primary" onClick={() => checkout(o.id)}>Checkout</button><button className="delete-button" title="Remove" onClick={() => deleteCartItem(o.id)}><Trash2 size={15}/></button></td></tr>)}
            {!cart.length && <tr><td colSpan="6" className="empty-cell">Your cart is empty.</td></tr>}
          </tbody></table></div>
          <div className="cart-total"><span>Cart total</span><strong>₹{cartTotal.toLocaleString('en-IN')}</strong></div>
        </div>
      </section>}

      {tab === 'orders' && <section>
        <div className="section-heading"><div><h2>Order history</h2><p>Orders placed by the selected customer.</p></div><span className="count-pill">{orders.length} orders</span></div>
        <div className="panel"><div className="panel-title"><div><h2>All customer orders</h2><p>Select a customer at the top-right to load their orders.</p></div></div>
          <div className="table-wrap"><table><thead><tr><th>ORDER ID</th><th>PRODUCT</th><th>QUANTITY</th><th>TOTAL</th><th>STATUS</th><th>DATE</th></tr></thead><tbody>
            {orders.map(o => <tr key={o.id}><td>#{o.id}</td><td className="strong-cell">{o.product?.name || `Product #${o.product?.id || ''}`}</td><td>{o.quantity}</td><td>₹{Number(o.totalAmount || 0).toLocaleString('en-IN')}</td><td><span className={`status ${o.status === 'PLACED' ? 'placed-status' : 'cart-status'}`}>{o.status}</span></td><td>{o.createdAt ? new Date(o.createdAt).toLocaleDateString('en-IN') : '—'}</td></tr>)}
            {!orders.length && <tr><td colSpan="6" className="empty-cell">No orders found for this customer.</td></tr>}
          </tbody></table></div>
        </div>
      </section>}
      <footer>ShopEase <span>•</span> React + Spring Boot + MySQL <span>•</span> Learning project</footer>
    </main>
  </div>
}

function Field({ label, wide, children }) {
  return <label className={`field ${wide ? 'wide' : ''}`}><span>{label}</span>{children}</label>
}
function Empty({ title, text }) {
  return <div className="empty-state"><Package size={28}/><h3>{title}</h3><p>{text}</p></div>
}
