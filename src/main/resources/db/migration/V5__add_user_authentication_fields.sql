-- Add authentication fields to users table
ALTER TABLE users ADD COLUMN role VARCHAR(50) NOT NULL DEFAULT 'USER';
ALTER TABLE users ADD COLUMN enabled BOOLEAN NOT NULL DEFAULT TRUE;


-- Insert default admin user
INSERT INTO users (username, password, role, enabled, created_at) 
VALUES ('admin', 'pass', 'ADMIN', TRUE, CURRENT_TIMESTAMP)