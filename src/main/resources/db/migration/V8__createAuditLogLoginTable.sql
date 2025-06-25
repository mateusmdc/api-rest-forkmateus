CREATE TABLE audit_log_login (
    id VARCHAR(36) PRIMARY KEY,
    user_name VARCHAR(255) NOT NULL,
    login_time TIMESTAMP NOT NULL,
    logout_time TIMESTAMP,
    ip_address VARCHAR(45),
    login_status VARCHAR(50) NOT NULL,     -- Resultado do login (ex.: SUCCESS, FAILURE)
    user_agent TEXT,                       -- Informações do agente de usuário (ex.: navegador)
    host_name VARCHAR(255),                -- Nome do host que fez o login
    server_name VARCHAR(255),              -- Nome do servidor que processou a requisição
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE OR REPLACE FUNCTION update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
   NEW.updated_at = NOW();
   RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_audit_log_timestamp
BEFORE UPDATE ON audit_log
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();