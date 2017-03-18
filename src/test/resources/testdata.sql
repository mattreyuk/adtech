insert into ad_size (ad_size_id,width,height) values (1,200,300);
insert into provider (provider_id,provider_name,url) values (10,"provider1","http://localhost:8060/p1");
insert into user (user_id,username) values (100,"user1");
insert into user_provider_assoc (user_provider_assoc_id,user_id,provider_id) values (1000,100,10);
insert into user_size_assoc (user_id,user_size_assoc_id,ad_size_id) values (100,10000,1);
insert into provider_size_assoc (provider_id,provider_size_assoc_id,ad_size_id) values (10,100000,1);
insert into ad_size (ad_size_id,width,height) values (2,400,600);
insert into user_size_assoc (user_id,user_size_assoc_id,ad_size_id) values (100,20000,2);
insert into provider (provider_id,provider_name,url) values (20,"provider2","http://localhost:8060/p2");
insert into user_provider_assoc (user_provider_assoc_id,user_id,provider_id) values (2000,100,20);
insert into provider_size_assoc (provider_id,provider_size_assoc_id,ad_size_id) values (20,200000,2);
insert into provider (provider_id,provider_name,url) values (30,"provider3","http://localhost:8060/p3");
insert into provider (provider_id,provider_name,url) values (40,"provider4","http://localhost:8060/p4");
insert into user_provider_assoc (user_provider_assoc_id,user_id,provider_id) values (3000,100,30);
insert into user_provider_assoc (user_provider_assoc_id,user_id,provider_id) values (4000,100,40);
insert into provider_size_assoc (provider_id,provider_size_assoc_id,ad_size_id) values (30,300000,1);
insert into provider_size_assoc (provider_id,provider_size_assoc_id,ad_size_id) values (40,400000,1);