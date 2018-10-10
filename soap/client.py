from suds.client import Client
url = 'http://localhost:8080/DistributedBankWeb/BankImplService?wsdl'
client = Client(url)

#result = client.service.createUser('vinicius', '123')
#print(result)

result = client.service.authenticate('vinicius', '123')
print(result)

#result = client.service.createAccount(123,'123')
#print(result)

result = client.service.deposit(100, 123, True)
print(result)

result = client.service.statement(123,'123')
print(result)

client.service.signOut()
