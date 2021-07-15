create schema medline_clustering;

fdddgyu 
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

create view medline_clustering.author_affiliation_split as 
select pmid,seqnum,seqnum2,token as affiliation
from
	medline.author_affiliation ,
	unnest(regexp_split_to_array(affiliation,'(?<!&#?[A-Za-z0-9]+); *')) s(token)
;

create view medline_clustering.author_affiliation_staging as
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
from medline.author_affiliation_split
;

create view medline_clustering.author_affiliation as
select
	pmid,
	seqnum,
	seqnum2,
	name,
	id
from medline_clustering.author_affiliation_staging,ror.organization
where author_affiliation_staging.slot1=organization.name
  and not exists (select * from ror.organization where slot2=name)
  and not exists (select * from ror.organization where slot3=name)
  and not exists (select * from ror.organization where slot4=name)
  and not exists (select * from ror.organization where slot5=name)
  and not exists (select * from ror.organization where slot6=name)
  and not exists (select * from ror.organization where slot7=name)
  and not exists (select * from ror.organization where slot8=name)
  and not exists (select * from ror.organization where slot9=name)
union
select
	pmid,
	seqnum,
	seqnum2,
	name,
	id
from medline_clustering.author_affiliation_staging,ror.organization
where author_affiliation_staging.slot2=organization.name
  and not exists (select * from ror.organization where slot3=name)
  and not exists (select * from ror.organization where slot4=name)
  and not exists (select * from ror.organization where slot5=name)
  and not exists (select * from ror.organization where slot6=name)
  and not exists (select * from ror.organization where slot7=name)
  and not exists (select * from ror.organization where slot8=name)
  and not exists (select * from ror.organization where slot9=name)
union
select
	pmid,
	seqnum,
	seqnum2,
	name,
	id
from medline_clustering.author_affiliation_staging,ror.organization
where author_affiliation_staging.slot3=organization.name
  and not exists (select * from ror.organization where slot4=name)
  and not exists (select * from ror.organization where slot5=name)
  and not exists (select * from ror.organization where slot6=name)
  and not exists (select * from ror.organization where slot7=name)
  and not exists (select * from ror.organization where slot8=name)
  and not exists (select * from ror.organization where slot9=name)
union
select
	pmid,
	seqnum,
	seqnum2,
	name,
	id
from medline_clustering.author_affiliation_staging,ror.organization
where author_affiliation_staging.slot4=organization.name
  and not exists (select * from ror.organization where slot5=name)
  and not exists (select * from ror.organization where slot6=name)
  and not exists (select * from ror.organization where slot7=name)
  and not exists (select * from ror.organization where slot8=name)
  and not exists (select * from ror.organization where slot9=name)
union
select
	pmid,
	seqnum,
	seqnum2,
	name,
	id
from medline_clustering.author_affiliation_staging,ror.organization
where author_affiliation_staging.slot5=organization.name
  and not exists (select * from ror.organization where slot6=name)
  and not exists (select * from ror.organization where slot7=name)
  and not exists (select * from ror.organization where slot8=name)
  and not exists (select * from ror.organization where slot9=name)
union
select
	pmid,
	seqnum,
	seqnum2,
	name,
	id
from medline_clustering.author_affiliation_staging,ror.organization
where author_affiliation_staging.slot6=organization.name
  and not exists (select * from ror.organization where slot7=name)
  and not exists (select * from ror.organization where slot8=name)
  and not exists (select * from ror.organization where slot9=name)
union
select
	pmid,
	seqnum,
	seqnum2,
	name,
	id
from medline_clustering.author_affiliation_staging,ror.organization
where author_affiliation_staging.slot7=organization.name
  and not exists (select * from ror.organization where slot8=name)
  and not exists (select * from ror.organization where slot9=name)
union
select
	pmid,
	seqnum,
	seqnum2,
	name,
	id
from medline_clustering.author_affiliation_staging,ror.organization
where author_affiliation_staging.slot8=organization.name
  and not exists (select * from ror.organization where slot9=name)
union
select
	pmid,
	seqnum,
	seqnum2,
	name,
	id
from medline_clustering.author_affiliation_staging,ror.organization
where author_affiliation_staging.slot9=organization.name
;

create materialized view testing as
select
	document_cluster.last_name,
	document_cluster.fore_name,
	document_cluster.cid,
	cluster_document.pmid,
	author.seqnum
from
	medline_clustering.document_cluster,
	medline_clustering.cluster_document,
	medline.author
where document_cluster.cid = cluster_document.cid
  and cluster_document.pmid = author.pmid
  and document_cluster.last_name = author.last_name
  and document_cluster.fore_name = author.fore_name
;

create index test1 on testing(pmid,seqnum);
