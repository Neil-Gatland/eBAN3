USE [RevShare]
GO
/****** Object:  StoredProcedure [dbo].[Get_Invoice_XML_All]    Script Date: 02/24/2011 14:05:41 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO





--exec Get_Invoice_XML '8200441A201011001'

CREATE proc [dbo].[Get_Invoice_XML_All] 
	@invoice_number varchar(20)
WITH RECOMPILE 
AS 
SET NOCOUNT ON

select 99 as Tag,
null as Parent,
null as [rs_invoices!99],
null as [rs_invoice!1!invoice_number!element],
null as [rs_invoice!1!invoice_type!element],
null as [rs_invoice!1!tax_point_date!element],
null as [rs_invoice!1!net_amount!element],
null as [rs_invoice!1!vat_amount!element],
null as [rs_invoice!1!gross_amount!element],
null as [rs_invoice!1!vat_rate!element],
null as [rs_invoice!1!start_date!element], 
null as [rs_invoice!1!end_date!element],
null as [master_account!2!master_account_number!element],
null as [master_account!2!frequency_code!element],
null as [call_month!3!value],
null as [call_month!3!call_month_literal!element],
null as [call_month!3!sum_total_calls!element],
null as [call_month!3!sum_total_duration!element],
null as [call_month!3!sum_net_amount!element],
null as [invoice_line!4!number_type!element],
null as [invoice_line!4!total_calls!element],
null as [invoice_line!4!total_duration!element],
null as [invoice_line!4!net_amount!element],
null as [provider!5!provider_name!element],
null as [provider!5!address_line1!element],
null as [provider!5!address_line2!element],
null as [provider!5!address_line3!element],
null as [provider!5!address_line4!element],
null as [provider!5!address_line5!element],
null as [provider!5!address_line6!element],
null as [provider!5!sap_vendor_number!element],
null as [provider!5!bank_sort_code!element],
null as [provider!5!bank_account_number!element],
null as [provider!5!vat_number!element],
null as [adjustment_line!6!adjustment_line_no],
null as [adjustment_line!6!adjustment_description!element],
null as [adjustment_line!6!adjustment_amount!element]
union
select 1 as Tag,
99 as Parent,
null,
i.invoice_number as [rs_invoice!1!invoice_number!element],
i.invoice_type as [rs_invoice!1!invoice_type!element],
convert(varchar(11), i.tax_point_date, 103) as [rs_invoice!1!tax_point_date!element],
convert(varchar,cast(i.net_amount as money),1) as [rs_invoice!1!net_amount!element],
convert(varchar,cast(i.vat_amount as money),1) as [rs_invoice!1!vat_amount!element],
convert(varchar,cast(i.gross_amount as money),1) as [rs_invoice!1!gross_amount!element],
convert(varchar,cast(i.vat_rate*100 as money),1) as [rs_invoice!1!vat_rate!element],
'01 ' + datename(month,convert(datetime,min(l.call_month)+'01',112)) + ' ' +
convert(char(4),datepart(year,convert(datetime,min(l.call_month)+'01',112))) as [rs_invoice!1!start_date!element], 
convert(char(2),datepart(day, dateadd(day, -1, dateadd(month, 1, convert(datetime,max(l.call_month)+'01',112)))))
+ ' ' +
datename(month,dateadd(day, -1, dateadd(month, 1, convert(datetime,max(l.call_month)+'01',112))))
+ ' ' +
convert(char(4),datepart(year,dateadd(day, -1, dateadd(month, 1, convert(datetime,max(l.call_month)+'01',112))))) as [rs_invoice!1!end_date!element],
null as [master_account!2!master_account_number!element],
null as [master_account!2!frequency_code!element],
null as [call_month!3!value],
null as [call_month!3!call_month_literal!element],
null as [call_month!3!sum_total_calls!element],
null as [call_month!3!sum_total_duration!element],
null as [call_month!3!sum_net_amount!element],
null as [invoice_line!4!number_type!element],
null as [invoice_line!4!total_calls!element],
null as [invoice_line!4!total_duration!element],
null as [invoice_line!4!net_amount!element],
null as [provider!5!provider_name!element],
null as [provider!5!address_line1!element],
null as [provider!5!address_line2!element],
null as [provider!5!address_line3!element],
null as [provider!5!address_line4!element],
null as [provider!5!address_line5!element],
null as [provider!5!address_line6!element],
null as [provider!5!sap_vendor_number!element],
null as [provider!5!bank_sort_code!element],
null as [provider!5!bank_account_number!element],
null as [provider!5!vat_number!element],
null as [adjustment_line!6!adjustment_line_no],
null as [adjustment_line!6!adjustment_description!element],
null as [adjustment_line!6!adjustment_amount!element]
from invoice i
left outer join invoice_line l
on i.invoice_number = l.invoice_number
where (i.invoice_number = @invoice_number or (i.produced = 'N' and @invoice_number = 'All'))

/*
from invoice i, invoice_line l
where i.invoice_number = @invoice_number
and i.invoice_number = l.invoice_number
*/
group by i.invoice_number, i.invoice_type, i.tax_point_date, i.net_amount, i.vat_amount, i.gross_amount, i.vat_rate 
union
select 2 as tag,
1 as parent,
null,
i.invoice_number,
null,
null,
null,
null,
null,
null,
null,
null,
m.master_account_number,
m.frequency_code,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null
from invoice i, master_account m
where (i.invoice_number = @invoice_number or (i.produced = 'N' and @invoice_number = 'All'))
and i.master_account_id = m.master_account_id
union
select 3 as tag,
1 as parent,
null,
i.invoice_number,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
c.call_month,
call_month_literal,
left(convert(varchar,cast(sum(l.total_calls) as money),1), len(convert(varchar,cast(sum(l.total_calls) as money),1))-3),
left(convert(varchar,cast(sum(l.total_duration) as money),1), len(convert(varchar,cast(sum(l.total_duration) as money),1))-3),
convert(varchar,cast(sum(l.net_amount) as money),1),
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null
from invoice i, invoice_line l, call_month_view c
where (i.invoice_number = @invoice_number or (i.produced = 'N' and @invoice_number = 'All'))
and i.invoice_number = l.invoice_number
and c.call_month = l.call_month
group by i.invoice_number, c.call_month, call_month_literal
union
select 4 as tag,
3 as parent,
null,
i.invoice_number,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
c.call_month,
call_month_literal,
null,
null,
null,
p.product_description,
left(convert(varchar,cast(l.total_calls as money),1), len(convert(varchar,cast(l.total_calls as money),1))-3),
left(convert(varchar,cast(l.total_duration as money),1), len(convert(varchar,cast(l.total_duration as money),1))-3),
convert(varchar,cast(l.net_amount as money),1),
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null
from invoice i, invoice_line l, call_month_view c, product p
where (i.invoice_number = @invoice_number or (i.produced = 'N' and @invoice_number = 'All'))
and i.invoice_number = l.invoice_number
and c.call_month = l.call_month
and p.product_code = l.product_code
union
select 5 as tag,
1 as parent,
null,
i.invoice_number,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
upper(p.provider_name),
upper(isnull(p.address_line1,'')),
upper(isnull(p.address_line2,'')),
upper(isnull(p.address_line3,'')),
upper(isnull(p.address_line4,'')),
upper(isnull(p.address_line5,'')),
upper(isnull(p.address_line6,'')),
isnull(p.sap_vendor_number,''),
isnull(p.bank_sort_code,''),
case when p.bank_account_number is null then '' else '****' + right(p.bank_account_number,4) end,
isnull(p.vat_number,''),
null,
null,
null
from invoice i, master_account m, provider p
where (i.invoice_number = @invoice_number or (i.produced = 'N' and @invoice_number = 'All'))
and i.master_account_id = m.master_account_id
and p.provider_id = m.provider_id
union
select 6 as tag,
1 as parent,
null,
i.invoice_number,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
null,
a.adjustment_line_no,
a.adjustment_description,
convert(varchar,cast(a.adjustment_amount as money),1)
from adjustment_line a, invoice i
where (i.invoice_number = @invoice_number or (i.produced = 'N' and @invoice_number = 'All'))
and a.invoice_number = i.invoice_number
order by [rs_invoice!1!invoice_number!element], [call_month!3!value], [adjustment_line!6!adjustment_line_no]
for xml explicit






