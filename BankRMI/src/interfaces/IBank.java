package interfaces;

public interface IBank {
	
	public Long createAccount(Long id, String password);
	public Boolean deposit(Double value, Long account);
	public Boolean withdraw(Long account, String password, Double value);
	public Boolean transfer(Long account, String password, Double value, Long anotherAccount);
	public String statement(Long account, String password);
	
}
