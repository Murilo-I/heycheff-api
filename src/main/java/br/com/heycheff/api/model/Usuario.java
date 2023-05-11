package br.com.heycheff.api.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String username;
	private String email;
	private String senha;
	private String nickname;
	private int funcoes;

	public Usuario(int id, String username, String email, String senha, String nickname, int funcoes) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.senha = senha;
		this.nickname = nickname;
		this.funcoes = funcoes;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getFuncoes() {
		return funcoes;
	}

	public void setFuncoes(int funcoes) {
		this.funcoes = funcoes;
	}
}
