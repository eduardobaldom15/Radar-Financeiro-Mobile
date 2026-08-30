import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import Dashboard from "./pages/Dashboard";
import ProtectedRoute from "./routes/ProtectedRoute";
import Cadastro from "./pages/Cadastro";
import Graficos from "./pages/Graficos";

export default function App() {
  return (
    <BrowserRouter>
      <Routes>

        <Route path="/" element={<Login />} />

        <Route path="/cadastro" element={<Cadastro />} />

        <Route
          path="/dashboard"
          element={
            <ProtectedRoute>
              <Dashboard />
            </ProtectedRoute>
          }
        />
        <Route
          path="/graficos"
          element={
            <ProtectedRoute>
              <Graficos />
            </ProtectedRoute>
          }
        />

      </Routes>
    </BrowserRouter>
  );
}