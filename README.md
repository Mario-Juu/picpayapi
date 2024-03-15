###  Projeto PicPay Simplificado API
## Descrição
Uma aplicação web onde são gerenciadas transações respeitando ACID feita em Spring no padrão MVC, o projeto conta com uma estrutura CRUD e comunicações via arquitetura REST. Conta com a elaboração de testes unitários com JUNit 5 e Mockito.

## Tecnologias
- Java
- SpringBoot
- H2 Database
- SpringData
- Jakarta
- JUnit 5
- Mockito

# Como usar
1. Abra sua IDE Java de preferência
2. Clone o repositório
   ```sh
   git clone [https://github.com/your_username_/Project-Name.git](https://github.com/Mario-Juu/picpayapi.git)
   ```
3. Dê run na aplicação.
4. Faça as requisições via Postman ou Insomnia.

# Moldes das requisições
**POST** CreateUser (http://localhost:8080/user/)
```sh
{
	"firstName": "Ddd",
	"lastName": "Ddd",
	"document": "1222",
	"email":"mariojralv006@gmail.com",
	"password": "123",
	"balance": 15
}
```
**GET** AllUsers (http://localhost:8080/user/)

**POST** CreateTransaction(http://localhost:8080/transaction)
```sh
{
	"senderId": 4,
	"receiverId": 1,
	"value": 10
}
```


 
