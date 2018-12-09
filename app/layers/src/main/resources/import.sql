-- orders
insert into orders(buyersemail) values ('josh@gmail.com');
insert into orders(buyersemail) values ('bill@gates.com');

-- ordered-products
insert into orders_orderedproducts(orders_id,name,price) values (1,'Orange', 2.2);
insert into orders_orderedproducts(orders_id,name,price) values (2,'Apple', 1.1);

-- products
insert into product(name,price) values ('Orange', 2.2);
insert into product(name,price) values ('Apple', 1.1);
insert into product(name,price) values ('Banana', 5.0);