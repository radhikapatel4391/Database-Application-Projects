select * from site_user;
select * from product;
select * from track;
select * from download;
select * from invoice;
select * from lineitem;

select user_id 
	from site_user 
		where email_address like '%gmail.com';

select product_id, quantity 
	from lineitem 
		where invoice_id=2;

select t.product_id,t.sample_filename,d.download_date
	from download d, track t
		where d.track_id = t.track_id
		AND user_id in
			(select user_id from site_user where email_address = 'andi@murach.com');
			
INSERT INTO invoice VALUES (2,2,timestamp '2010-09-02 00:00:00',9.99,'y');
INSERT INTO lineitem VALUES (1,2,2,15);
INSERT INTO lineitem VALUES (2,2,4,25);

select i.invoice_id, i.invoice_date 
	from invoice i 
		where user_id 
			IN  (select user_id 
				from site_user 
					where email_address = 'andi@murach.com');
			
select product_id 
	from lineitem 
		where invoice_id 
			IN (select invoice_id 
					from invoice 
						where user_id 
							IN (select user_id 
									from site_user 
										where email_address = 'andi@murach.com'));

select distinct x.product_id
	from 
		(select t.product_id,d.download_date
				from track t,download d
					where t.track_id=d.track_id) AS x,		 
		(select l.product_id, i.invoice_date
				from invoice i, lineitem l
					where i.invoice_id=l.invoice_id) AS y
			where y.product_id = x.product_id 
				and x.download_date < y.invoice_date;

select t.sample_filename 
	from track t,	
			(select  x.track_id, MAX(x.count)
				from 
					(select count(*) as count,track_id
						from download
							group by track_id) as x) as y
				where y.track_id = t.track_id; 
				

select count(*), t.product_id
	from track t,		
			(select track_id
				from download
					where download_date >= '2009-08-01 00:00:00') as  x
		where
			x.track_id = t.track_id 
				AND
					t.product_id IN (2,3)
		group by t.product_id;
		
select count(*), t.product_id
	from track t,		
			(select track_id
				from download
					where download_date >= '2009-08-01 00:00:00') as  x
		where
			x.track_id = t.track_id 
				
		group by t.product_id;		
  