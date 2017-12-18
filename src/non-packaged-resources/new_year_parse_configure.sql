create table request_temp (pmid int);

insert into request_temp
select pmid
from medline18.article
where not exists (select pmid from medline17.article as art where art.pmid=article.pmid);

insert into request_temp
select article.pmid
from
	medline18.article,
	medline17.article as art
where art.pmid=article.pmid
  and art.title != article.title;

insert into request_temp
select article.pmid
from
	(select pmid,max(seqnum) as max from medline18.abstr group by 1) as article,
	(select pmid,max(seqnum) as max from medline17.abstr group by 1) as art
where article.pmid=art.pmid
  and article.max!= art.max;

insert into request_temp
select article.pmid
from
	medline18.abstr as article,
	medline17.abstr as art
where art.pmid=article.pmid
  and art.seqnum = article.seqnum
  and art.abstract_text != article.abstract_text;

truncate parse_request;
insert into parse_request select distinct pmid from request_temp;
drop table request_temp;
