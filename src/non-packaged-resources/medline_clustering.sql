create schema medline_clustering;

create sequence medline_clustering.seqnum;

create materialized view medline_clustering.author_count as
select
	last_name,
	fore_name,
	count(*),
	false as completed
from medline.author
group by 1,2
;

===============

select * from medline.author_affiliation , unnest(string_to_array(regexp_replace(affiliation,'&amp;','&','g'),';')) s(token) where affiliation~'&amp' limit 10;

select pmid,seqnum,seqnum2,token,affiliation,regexp_replace(token,'^ *& *','') from
(select *
from
	medline.author_affiliation ,
	unnest(string_to_array(regexp_replace(affiliation,'&amp;','&','g'),';')) s(token)
where affiliation~'[^p];.&') as foo
limit 100;

create view medline_clustering.affiliation as
select
	pmid,
	seqnum,
	seqnum2,
	affiliation,
	trim(split_part(affiliation,',',1)) as slot1,
	trim(split_part(affiliation,',',2)) as slot2,
	trim(split_part(affiliation,',',3)) as slot3,
	trim(split_part(affiliation,',',4)) as slot4,
	trim(split_part(affiliation,',',5)) as slot5,
	trim(split_part(affiliation,',',6)) as slot6,
	trim(split_part(affiliation,',',7)) as slot7,
	trim(split_part(affiliation,',',8)) as slot8,
	trim(split_part(affiliation,',',9)) as slot9
from medline.author_affiliation
;

select
	affiliation.*,
	name,
	id
from medline_clustering.affiliation,ror.organization
where affiliation.slot1=organization.name
  and not exists (select * from ror.organization where slot2=name)
limit 100
;

select
	affiliation.*,
	name,
	id
from medline_clustering.affiliation,ror.organization
where affiliation.slot2=organization.name
  and not exists (select * from ror.organization where slot3=name)
limit 100
;

select
	affiliation.*,
	name,
	id
from medline_clustering.affiliation,ror.organization
where affiliation.slot3=organization.name
  and not exists (select * from ror.organization where slot4=name)
limit 100
;

select
	affiliation.*,
	name,
	id
from medline_clustering.affiliation,ror.organization
where affiliation.slot4=organization.name
  and not exists (select * from ror.organization where slot5=name)
limit 100
;

select
	affiliation.*,
	name,
	id
from medline_clustering.affiliation,ror.organization
where affiliation.slot5=organization.name
  and not exists (select * from ror.organization where slot6=name)
limit 100
;

select
	affiliation.*,
	name,
	id
from medline_clustering.affiliation,ror.organization
where affiliation.slot6=organization.name
  and not exists (select * from ror.organization where slot7=name)
limit 100
;

select
	affiliation.*,
	name,
	id
from medline_clustering.affiliation,ror.organization
where affiliation.slot7=organization.name
  and not exists (select * from ror.organization where slot8=name)
limit 100
;

select
	affiliation.*,
	name,
	id
from medline_clustering.affiliation,ror.organization
where affiliation.slot8=organization.name
  and not exists (select * from ror.organization where slot9=name)
limit 100
;

select
	affiliation.*,
	name,
	id
from medline_clustering.affiliation,ror.organization
where affiliation.slot9=organization.name
limit 100
;
