# ShopEase React Frontend

React + Vite frontend for the Spring Boot E-Commerce backend.

## Requirements
- Node.js and npm
- Spring Boot backend running at http://localhost:8143
- Backend API routes:
  - `/api/products` (GET, POST)
  - `/api/customers` (GET, POST)
  - `/api/orders/cart` (POST)
  - `/api/orders/cart/{customerId}` (GET)
  - `/api/orders/checkout/{orderId}` (POST)
  - `/api/orders/customer/{customerId}` (GET)
  - `/api/orders/{orderId}` (DELETE)

## Run
1. Extract this ZIP.
2. Open the extracted `ecommerce-react-frontend` folder in VS Code.
3. Open Terminal in that folder.
4. Run:
   ```bash
   npm install
   npm run dev
   ```
5. Open the URL printed by Vite, normally `http://localhost:5173`.

## Backend CORS
The backend must allow the Vite origin `http://localhost:5173`. Add a global CORS configuration or `@CrossOrigin(origins = "http://localhost:5173")` to controllers.

## API base URL
The frontend currently calls `http://localhost:8143/api`. If your backend uses a different port, update `API` at the top of `src/App.jsx`.

## Important
This UI expects the backend JSON fields to match the generated project:
- Product: `id`, `name`, `description`, `price`, `stock`
- Customer: `id`, `name`, `email`, `phone`
- CustomerOrder: `id`, `customer`, `product`, `quantity`, `totalAmount`, `status`, `createdAt`

If your controller paths or model field names differ, update the frontend API calls/field names accordingly.
