import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import axios from "axios";
import "./../styles/login.css";
import logo from "../assets/logo.png";

export default function Login() {
  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const [erro, setErro] = useState("");

  const { login } = useAuth();
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();

    try {
      const response = await axios.post(
        "http://localhost:5279/api/auth/login",
        { email, senha }
      );

      login(response.data.token); 
      navigate("/dashboard");

    } catch (err) {
      setErro(err.response?.data || "Erro ao fazer login");
    }
  };

    return (

        <div className="login-container">

            <div className="login-card">

                <img
                    src={logo}
                    className="login-logo"
                />

                <h2>Radar Financeiro</h2>

                <p>Gerenciamento de Projetos de Pesquisa</p>

                <form onSubmit={handleLogin}>

                    <input
                        placeholder="Email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                    />

                    <input
                        type="password"
                        placeholder="Senha"
                        value={senha}
                        onChange={(e) => setSenha(e.target.value)}
                    />

                    <button type="submit">

                        Entrar

                    </button>

                </form>

                {erro &&

                    <p style={{ color: "red" }}>

                        {erro}

                    </p>

                }

                <div className="login-link">

                    Não possui conta?

                    <br />

                    <a href="/cadastro">

                        Cadastre-se

                    </a>

                </div>

            </div>

        </div>

    );
}