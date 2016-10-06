function Calculate_VAT(Amount,VAT_Indicator,Tax_Rate)
{
  if (document.all.VAT_Indicator.value=="Y")
  {
    document.all.VAT_Amount.value=Math.round(document.all.Gross_Amount.value/100*parseFloat(document.all.Local_Tax_Rateh.value)*100)/100;
    document.all.Total_Amount.value=Math.round((document.all.Gross_Amount.value/100*parseFloat(document.all.Local_Tax_Rateh.value))*100)/100+parseFloat(Amount);
    document.all.VAT_Amounth.value=Math.round(document.all.Gross_Amount.value/100*parseFloat(document.all.Local_Tax_Rateh.value)*100)/100;
    document.all.Total_Amounth.value=Math.round((document.all.Gross_Amount.value/100*parseFloat(document.all.Local_Tax_Rateh.value))*100)/100+parseFloat(Amount);
  }
  else
  {
    document.all.VAT_Amount.value=0.0;
    document.all.Total_Amount.value=document.all.Gross_Amount.value;
    document.all.VAT_Amounth.value=0.0;
    document.all.Total_Amounth.value=document.all.Gross_Amount.value;
  }
}
function checksubmit()
{
   if (document.all.Split_Sites.value != "")
   {
     document.forms.ChargeBAN.submit();		
   }
}