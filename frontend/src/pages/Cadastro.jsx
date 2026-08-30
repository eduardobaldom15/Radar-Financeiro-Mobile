import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "./../styles/cadastro.css";
import logo from "../assets/logo.png";

export default function Cadastro() {
    const navigate = useNavigate();

    const [form, setForm] = useState({
        nome: "",
        email: "",
        curso: "",
        departamento: "",
        senha: ""
    });

    const [erro, setErro] = useState("");
    const [sucesso, setSucesso] = useState("");

    const handleChange = (e) => {
        setForm({
            ...form,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (
            !form.nome.trim() ||
            !form.email.trim() ||
            !form.curso.trim() ||
            !form.departamento.trim() ||
            !form.senha.trim()
        ) {
            setErro("Preencha todos os campos.");
            return;
        }

        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

        if (!emailRegex.test(form.email)) {
            setErro("Digite um e-mail válido.");
            return;
        }

        if (form.senha.length < 6) {
            setErro("A senha deve possuir pelo menos 6 caracteres.");
            return;
        }

        try {
            await axios.post(
                "http://localhost:5279/api/Pesquisadores",
                form
            );

            setSucesso("Cadastro realizado com sucesso!");

            setTimeout(() => {
                navigate("/");
            }, 1500);

        } catch (err) {
            setErro("Erro ao cadastrar usuário");
        }
    };

    return (
        <div className="cadastro-container">

            <div className="cadastro-card">

                <img
                    src={logo}
                    alt="Logo"
                    className="cadastro-logo"
                />

                <h2>Cadastro</h2>

                <p>Crie sua conta para acessar o sistema</p>

                <form onSubmit={handleSubmit}>

                    <input
                        name="nome"
                        placeholder="Nome"
                        onChange={handleChange}
                    />

                    <input
                        name="email"
                        placeholder="Email"
                        onChange={handleChange}
                    />

                    <input
                        name="curso"
                        placeholder="Curso"
                        onChange={handleChange}
                    />

                    <input
                        name="departamento"
                        placeholder="Departamento"
                        onChange={handleChange}
                    />

                    <input
                        type="password"
                        name="senha"
                        placeholder="Senha"
                        onChange={handleChange}
                    />

                    <button type="submit">
                        Cadastrar
                    </button>

                </form>

                {erro && (
                    <p style={{ color: "red" }}>
                        {erro}
                    </p>
                )}

                {sucesso && (
                    <p style={{ color: "green" }}>
                        {sucesso}
                    </p>
                )}

                <div className="cadastro-link">

                    Já possui uma conta?

                    <br />

                    <a href="/">
                        Entrar
                    </a>

                </div>

            </div>

        </div>
    );
}