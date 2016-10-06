<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0"
      xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
      xmlns:fo="http://www.w3.org/1999/XSL/Format">
  <xsl:output method="xml" indent="yes"/>
  <xsl:template match="/">
    <fo:root>
      <fo:layout-master-set>
        <fo:simple-page-master master-name="front-page"
              page-height="29.7cm"
              page-width="21.0cm"
              margin-top="0cm"
              margin-bottom="0cm"
              margin-right="0cm"
              margin-left="0.5cm">
          <fo:region-body
              margin-bottom="4cm">
          </fo:region-body>
          <fo:region-after extent="3.5cm"/>
        </fo:simple-page-master>
        <fo:simple-page-master master-name="detail-page"
              page-height="29.7cm"
              page-width="21.0cm"
              margin-top="0cm"
              margin-bottom="0cm"
              margin-right="0cm"
              margin-left="0.5cm">
          <fo:region-body
              margin-top="4cm">
          </fo:region-body>
          <fo:region-before extent="3.5cm" display-align="before"/>
        </fo:simple-page-master>
        <fo:page-sequence-master master-name="detail-sequence">
          <fo:repeatable-page-master-reference master-reference="detail-page" />
        </fo:page-sequence-master>
      </fo:layout-master-set>
      <fo:page-sequence master-reference="front-page">
        <fo:static-content flow-name="xsl-region-after">
          <fo:block>
            <fo:table table-layout="fixed" width="100%">
              <fo:table-column column-width="75%"/>
              <fo:table-column column-width="25%"/>
              <fo:table-body>
                <fo:table-row>
                  <fo:table-cell number-columns-spanned="2">
                    <fo:block font="14pt Helvetica">
                      <fo:inline font-weight="bold" text-decoration="underline">THE VAT SHOWN IS YOUR OUTPUT TAX DUE TO HM REVENUE &amp; CUSTOMS</fo:inline>
                    </fo:block>
                  </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                  <fo:table-cell number-columns-spanned="2" height="1cm">
                    <fo:block/>
                  </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                  <fo:table-cell number-rows-spanned="4">
                    <fo:block/>
                  </fo:table-cell>
                  <fo:table-cell>
                      <fo:block font="6pt Helvetica">
                        <fo:inline font-weight="bold"><xsl:value-of select="rs_invoice/company_address/company_name"/></fo:inline>
                      </fo:block>
                  </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                  <fo:table-cell>
                      <fo:block font="6pt Helvetica">
                        <xsl:value-of select="rs_invoice/company_address/company_address_literal"/>
                      </fo:block>
                  </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                  <fo:table-cell>
                      <fo:block font="6pt Helvetica">
                        <xsl:value-of select="rs_invoice/company_address/company_details"/>
                      </fo:block>
                  </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                  <fo:table-cell>
                      <fo:block font="6pt Helvetica">
                        <xsl:value-of select="rs_invoice/company_address/vat_details"/>
                      </fo:block>
                  </fo:table-cell>
                </fo:table-row>
              </fo:table-body>
            </fo:table>
          </fo:block>
        </fo:static-content>
        <fo:flow flow-name="xsl-region-body">
          <fo:block>
            <fo:table table-layout="fixed" width="100%">
              <fo:table-body>
                <fo:table-row>
                  <fo:table-cell>
                    <fo:block font="30pt Times" text-align="left" padding-top="1cm" padding-bottom="0.5cm">
                      <fo:inline color="#7C9EDD" font-weight="bold">Tax Invoice</fo:inline>
                    </fo:block>
                  </fo:table-cell>
                  <fo:table-cell text-align="right">
                    <fo:block>
                      <fo:external-graphic content-height="2cm" src="url('file:///f:/test/fop/images/cw_blue.jpg')"/>
                    </fo:block>
                  </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                  <fo:table-cell>
                    <fo:block font="12pt Helvetica" padding-bottom="1cm">
                      <fo:inline font-style="italic" font-weight="bold">Supplier's Name and Registered Office</fo:inline>
                    </fo:block>
                  </fo:table-cell>
                  <fo:table-cell>
                    <fo:block></fo:block>
                  </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                  <fo:table-cell height="0.5cm" padding-left="1cm">
                    <fo:block font="10pt Helvetica">
                      <fo:inline font-weight="bold"><xsl:value-of select="rs_invoice/provider/name"/></fo:inline>
                    </fo:block>
                  </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                  <fo:table-cell height="0.5cm" padding-left="1cm">
                    <fo:block font="10pt Helvetica">
                      <fo:inline font-weight="bold"><xsl:value-of select="rs_invoice/provider/address_line1"/></fo:inline>
                    </fo:block>
                  </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                  <fo:table-cell height="0.5cm" padding-left="1cm">
                    <fo:block font="10pt Helvetica">
                      <fo:inline font-weight="bold"><xsl:value-of select="rs_invoice/provider/address_line2"/></fo:inline>
                    </fo:block>
                  </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                  <fo:table-cell height="0.5cm" padding-left="1cm">
                    <fo:block font="10pt Helvetica">
                      <fo:inline font-weight="bold"><xsl:value-of select="rs_invoice/provider/address_line3"/></fo:inline>
                    </fo:block>
                  </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                  <fo:table-cell height="0.5cm" padding-left="1cm">
                    <fo:block font="10pt Helvetica">
                      <fo:inline font-weight="bold"><xsl:value-of select="rs_invoice/provider/address_line4"/></fo:inline>
                    </fo:block>
                  </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                  <fo:table-cell height="0.5cm" padding-left="1cm">
                    <fo:block font="10pt Helvetica">
                      <fo:inline font-weight="bold"><xsl:value-of select="rs_invoice/provider/address_line5"/></fo:inline>
                    </fo:block>
                  </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                  <fo:table-cell padding-bottom="1cm" height="0.5cm" padding-left="1cm">
                    <fo:block font="10pt Helvetica">
                      <fo:inline font-weight="bold"><xsl:value-of select="rs_invoice/provider/address_line6"/></fo:inline>
                    </fo:block>
                  </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                  <fo:table-cell number-columns-spanned="2">
                    <fo:table table-layout="fixed" width="100%">
                      <fo:table-column column-width="20%"/>
                      <fo:table-column column-width="20%"/>
                      <fo:table-column column-width="12%"/>
                      <fo:table-column column-width="48%"/>
                      <fo:table-body>
                        <fo:table-row>
                          <fo:table-cell padding-bottom="0.25cm">
                            <fo:block font="10pt Helvetica">
                              <fo:inline font-weight="bold">Selfbilled Invoice No.:</fo:inline>
                            </fo:block>
                          </fo:table-cell>
                          <fo:table-cell>
                            <fo:block font="10pt Helvetica">
                              <fo:inline font-weight="bold"><xsl:value-of select="rs_invoice/invoice_number"/></fo:inline>
                            </fo:block>
                          </fo:table-cell>
                          <fo:table-cell>
                            <fo:block font="10pt Helvetica">
                              <fo:inline font-weight="bold">Invoice Date:</fo:inline>
                            </fo:block>
                          </fo:table-cell>
                          <fo:table-cell>
                            <fo:block font="10pt Helvetica">
                              <fo:inline font-weight="bold"><xsl:value-of select="rs_invoice/tax_point_date"/></fo:inline>
                            </fo:block>
                          </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                          <fo:table-cell padding-bottom="0.25cm">
                            <fo:block font="10pt Helvetica">
                              <fo:inline font-weight="bold">Account Number:</fo:inline>
                            </fo:block>
                          </fo:table-cell>
                          <fo:table-cell>
                            <fo:block font="10pt Helvetica">
                              <fo:inline font-weight="bold"><xsl:value-of select="rs_invoice/master_account/master_account_number"/></fo:inline>
                            </fo:block>
                          </fo:table-cell>
                          <fo:table-cell>
                            <fo:block font="10pt Helvetica">
                              <fo:inline font-weight="bold">Supply Type:</fo:inline>
                            </fo:block>
                          </fo:table-cell>
                          <fo:table-cell>
                            <fo:block font="10pt Helvetica">
                              <fo:inline font-weight="bold">
                                <xsl:choose>
                                  <xsl:when test="rs_invoice/invoice_type='Invoice'">
                                    Consolidated Revenue Share Services Invoice
                                  </xsl:when>
                                  <xsl:otherwise>
                                    Revenue Share Services - Adjustment Invoice
                                  </xsl:otherwise>
                                </xsl:choose>
                              </fo:inline>
                            </fo:block>
                          </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                          <fo:table-cell padding-bottom="2cm">
                            <fo:block font="10pt Helvetica">
                              <fo:inline font-weight="bold">Supplier Number:</fo:inline>
                            </fo:block>
                          </fo:table-cell>
                          <fo:table-cell>
                            <fo:block font="10pt Helvetica">
                              <fo:inline font-weight="bold"><xsl:value-of select="rs_invoice/provider/sap_vendor_number"/></fo:inline>
                            </fo:block>
                          </fo:table-cell>
                          <fo:table-cell number-columns-spanned="2">
                            <fo:block font="10pt Helvetica">
                              <fo:inline font-weight="bold">
                                <xsl:choose>
                                  <xsl:when test="rs_invoice/invoice_type='Invoice'">
                                    Page <fo:page-number/> of <fo:page-number-citation ref-id="last-page"/>
                                  </xsl:when>
                                  <xsl:otherwise/>
                                </xsl:choose>
                              </fo:inline>
                            </fo:block>
                          </fo:table-cell>
                        </fo:table-row>
                      </fo:table-body>
                    </fo:table>
                  </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                  <fo:table-cell number-columns-spanned="2" padding-bottom="1cm">
                    <fo:table table-layout="fixed" width="80%">
                      <fo:table-body>
                        <fo:table-row>
                          <fo:table-cell text-align="right">
                            <fo:table table-layout="fixed" width="100%" background-color="#CCDAF2">
                              <fo:table-column column-width="15%"/>
                              <fo:table-column column-width="25%"/>
                              <fo:table-column column-width="30%"/>
                              <fo:table-column column-width="5%"/>
                              <fo:table-column column-width="20%"/>
                              <fo:table-column column-width="5%"/>
                              <fo:table-body>
                                <fo:table-row>
                                  <fo:table-cell text-align="right" height="0.6cm" number-columns-spanned="6">
                                    <fo:block/>
                                  </fo:table-cell>
                                </fo:table-row>
                                <xsl:choose>
                                  <xsl:when test="rs_invoice/invoice_type='Invoice'">
                                    <fo:table-row>
                                      <fo:table-cell text-align="right" height="0.6cm" number-columns-spanned="3">
                                        <fo:block font="10pt Helvetica">
                                          <fo:inline font-weight="bold">For the period <xsl:value-of select="rs_invoice/start_date"/>
                                          to <xsl:value-of select="rs_invoice/end_date"/></fo:inline>
                                        </fo:block>
                                      </fo:table-cell>
                                      <fo:table-cell text-align="right" height="0.6cm" padding-right="0.5%">
                                        <fo:block font="10pt Helvetica">
                                          <fo:inline font-weight="bold">£</fo:inline>
                                        </fo:block>
                                      </fo:table-cell>
                                      <fo:table-cell
                                        margin-right="0.1cm"
                                        border-top="0.5pt solid black"
                                        border-left="0.5pt solid black"
                                        border-bottom="2pt solid black"
                                        border-right="2pt solid black"
                                        text-align="right">
                                        <fo:block font="10pt Helvetica">
                                          <fo:inline font-weight="bold"><xsl:value-of select="rs_invoice/net_amount"/></fo:inline>
                                        </fo:block>
                                      </fo:table-cell>
                                      <fo:table-cell>
                                        <fo:block/>
                                      </fo:table-cell>
                                    </fo:table-row>
                                  </xsl:when>
                                  <xsl:otherwise>
                                    <xsl:for-each select="rs_invoice/adjustment_line">
                                      <fo:table-row>
                                        <fo:table-cell text-align="left" height="0.6cm" number-columns-spanned="3">
                                          <fo:block font="8pt Helvetica">
                                            <fo:inline font-weight="bold">
                                              <xsl:value-of select="adjustment_description"/>
                                            </fo:inline>
                                          </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="right" height="0.6cm" padding-right="0.5%">
                                          <fo:block font="10pt Helvetica">
                                            <fo:inline font-weight="bold">£</fo:inline>
                                          </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell
                                          height="0.6cm"
                                          margin-right="0.1cm"
                                          border-top="0.5pt solid black"
                                          border-left="0.5pt solid black"
                                          border-bottom="2pt solid black"
                                          border-right="2pt solid black"
                                          text-align="right">
                                          <fo:block font="10pt Helvetica">
                                            <fo:inline font-weight="bold"><xsl:value-of select="adjustment_amount"/></fo:inline>
                                          </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell>
                                          <fo:block/>
                                        </fo:table-cell>
                                      </fo:table-row>
                                      <fo:table-row>
                                        <fo:table-cell text-align="right" height="0.2cm" number-columns-spanned="6">
                                          <fo:block/>
                                        </fo:table-cell>
                                      </fo:table-row>
                                    </xsl:for-each>
                                  </xsl:otherwise>
                                </xsl:choose>
                                <fo:table-row>
                                  <fo:table-cell text-align="right" height="0.6cm" number-columns-spanned="6">
                                    <fo:block/>
                                  </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>
                                  <fo:table-cell text-align="right" height="0.6cm" number-columns-spanned="6" background-color="white">
                                    <fo:block/>
                                  </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>
                                  <fo:table-cell text-align="left" height="0.6cm" number-columns-spanned="6">
                                    <fo:block font="10pt Helvetica">
                                     <fo:inline font-style="italic" font-weight="bold">Bank Account Details</fo:inline>
                                    </fo:block>
                                  </fo:table-cell>
                                 </fo:table-row>
                                 <fo:table-row>
                                   <fo:table-cell text-align="left" height="0.6cm">
                                     <fo:block font="10pt Helvetica">
                                       <fo:inline font-weight="bold">Sort Code:</fo:inline>
                                     </fo:block>
                                   </fo:table-cell>
                                  <fo:table-cell text-align="left" height="0.6cm">
                                     <fo:block font="10pt Helvetica">
                                       <fo:inline font-weight="bold"><xsl:value-of select="rs_invoice/provider/bank_sort_code"/></fo:inline>
                                     </fo:block>
                                  </fo:table-cell>
                                   <fo:table-cell text-align="left" height="0.6cm">
                                     <fo:block font="10pt Helvetica">
                                       <fo:inline font-weight="bold">Total Amount excluding VAT</fo:inline>
                                     </fo:block>
                                   </fo:table-cell>
                                   <fo:table-cell text-align="right" height="0.6cm" padding-right="0.5%">
                                     <fo:block font="10pt Helvetica">
                                       <fo:inline font-weight="bold">£</fo:inline>
                                     </fo:block>
                                   </fo:table-cell>
                                   <fo:table-cell
                                      height="0.6cm"
                                      margin-right="0.1cm"
                                      border-top="0.5pt solid black"
                                      border-left="0.5pt solid black"
                                      border-bottom="2pt solid black"
                                      border-right="2pt solid black"
                                      text-align="right">
                                    <fo:block font="10pt Helvetica">
                                      <fo:inline font-weight="bold"><xsl:value-of select="rs_invoice/net_amount"/></fo:inline>
                                    </fo:block>
                                  </fo:table-cell>
                                  <fo:table-cell text-align="right" height="0.6cm">
                                    <fo:block/>
                                  </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>
                                  <fo:table-cell text-align="right" height="0.2cm" number-columns-spanned="6">
                                    <fo:block/>
                                  </fo:table-cell>
                                </fo:table-row>
                                 <fo:table-row>
                                   <fo:table-cell text-align="left" height="0.6cm">
                                     <fo:block font="10pt Helvetica">
                                       <fo:inline font-weight="bold">Account No.:</fo:inline>
                                     </fo:block>
                                   </fo:table-cell>
                                  <fo:table-cell text-align="left" height="0.6cm">
                                     <fo:block font="10pt Helvetica">
                                       <fo:inline font-weight="bold"><xsl:value-of select="rs_invoice/provider/bank_account_number"/></fo:inline>
                                     </fo:block>
                                  </fo:table-cell>
                                   <fo:table-cell text-align="left" height="0.6cm">
                                     <fo:block font="10pt Helvetica">
                                       <fo:inline font-weight="bold">VAT @ <xsl:value-of select="rs_invoice/vat_rate"/>%</fo:inline>
                                     </fo:block>
                                   </fo:table-cell>
                                   <fo:table-cell text-align="right" height="0.6cm" padding-right="0.5%">
                                     <fo:block font="10pt Helvetica">
                                       <fo:inline font-weight="bold">£</fo:inline>
                                     </fo:block>
                                   </fo:table-cell>
                                   <fo:table-cell
                                      margin-right="0.1cm"
                                      border-top="0.5pt solid black"
                                      border-left="0.5pt solid black"
                                      border-bottom="2pt solid black"
                                      border-right="2pt solid black"
                                      text-align="right">
                                    <fo:block font="10pt Helvetica">
                                      <fo:inline font-weight="bold"><xsl:value-of select="rs_invoice/vat_amount"/></fo:inline>
                                    </fo:block>
                                  </fo:table-cell>
                                  <fo:table-cell text-align="right" height="0.6cm">
                                    <fo:block/>
                                  </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>
                                  <fo:table-cell text-align="right" height="0.2cm" number-columns-spanned="6">
                                    <fo:block/>
                                  </fo:table-cell>
                                </fo:table-row>
                                 <fo:table-row>
                                   <fo:table-cell text-align="left" height="0.6cm">
                                     <fo:block font="10pt Helvetica">
                                       <fo:inline font-weight="bold">VAT No.:</fo:inline>
                                     </fo:block>
                                   </fo:table-cell>
                                  <fo:table-cell text-align="left" height="0.6cm">
                                     <fo:block font="10pt Helvetica">
                                       <fo:inline font-weight="bold"><xsl:value-of select="rs_invoice/provider/vat_number"/></fo:inline>
                                     </fo:block>
                                  </fo:table-cell>
                                   <fo:table-cell text-align="left" height="0.6cm">
                                     <fo:block font="10pt Helvetica">
                                       <fo:inline font-weight="bold">Total Amount including VAT</fo:inline>
                                     </fo:block>
                                   </fo:table-cell>
                                   <fo:table-cell text-align="right" height="0.6cm" padding-right="0.5%">
                                     <fo:block font="10pt Helvetica">
                                       <fo:inline font-weight="bold">£</fo:inline>
                                     </fo:block>
                                   </fo:table-cell>
                                   <fo:table-cell
                                      margin-right="0.1cm"
                                      border-top="0.5pt solid black"
                                      border-left="0.5pt solid black"
                                      border-bottom="2pt solid black"
                                      border-right="2pt solid black"
                                      text-align="right">
                                    <fo:block font="10pt Helvetica">
                                      <fo:inline font-weight="bold"><xsl:value-of select="rs_invoice/gross_amount"/></fo:inline>
                                    </fo:block>
                                  </fo:table-cell>
                                  <fo:table-cell text-align="right" height="0.6cm">
                                    <fo:block/>
                                  </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>
                                  <fo:table-cell text-align="right" height="0.6cm" number-columns-spanned="6">
                                    <fo:block/>
                                  </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>
                                  <fo:table-cell text-align="right" height="1.2cm" number-columns-spanned="6" background-color="white">
                                    <fo:block/>
                                  </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>
                                  <fo:table-cell text-align="left" number-columns-spanned="6" padding="0.6cm">
                                    <fo:block font="10pt Helvetica">
                                     <fo:inline font-weight="bold"><xsl:value-of select="rs_invoice/invoice_text/text"/></fo:inline>
                                    </fo:block>
                                  </fo:table-cell>
                                 </fo:table-row>
                              </fo:table-body>
                            </fo:table>
                          </fo:table-cell>
                       </fo:table-row>
                      </fo:table-body>
                    </fo:table>
                  </fo:table-cell>
                </fo:table-row>
              </fo:table-body>
            </fo:table>
          </fo:block>
        </fo:flow>
      </fo:page-sequence>
      <xsl:choose>
        <xsl:when test="rs_invoice/invoice_type='Invoice'">
          <fo:page-sequence master-reference="detail-sequence">
            <fo:static-content flow-name="xsl-region-before">
              <fo:block>
                <fo:table table-layout="fixed" width="100%">
                  <fo:table-column column-width="30%"/>
                  <fo:table-column column-width="30%"/>
                  <fo:table-column column-width="20%"/>
                  <fo:table-column column-width="20%"/>
                  <fo:table-body>
                    <fo:table-row>
                      <fo:table-cell number-rows-spanned="4">
                        <fo:block>
                          <fo:external-graphic content-height="2cm" src="url('file:///f:/test/fop/images/cw_blue.jpg')"/>
                        </fo:block>
                      </fo:table-cell>
                      <fo:table-cell number-columns-spanned="3" height="0.5cm">
                        <fo:block/>
                      </fo:table-cell>
                    </fo:table-row>
                    <fo:table-row>
                      <fo:table-cell>
                        <fo:block/>
                      </fo:table-cell>
                      <fo:table-cell height="0.5cm">
                        <fo:block font="10pt Helvetica">
                          <fo:inline font-weight="bold">Selfbilled Invoice No.:</fo:inline>
                        </fo:block>
                      </fo:table-cell>
                      <fo:table-cell>
                        <fo:block font="10pt Helvetica">
                          <fo:inline font-weight="bold"><xsl:value-of select="rs_invoice/invoice_number"/></fo:inline>
                        </fo:block>
                      </fo:table-cell>
                    </fo:table-row>
                    <fo:table-row>
                      <fo:table-cell>
                        <fo:block/>
                      </fo:table-cell>
                      <fo:table-cell height="0.5cm">
                        <fo:block font="10pt Helvetica">
                          <fo:inline font-weight="bold">Account Number:</fo:inline>
                        </fo:block>
                      </fo:table-cell>
                      <fo:table-cell height="0.5cm">
                        <fo:block font="10pt Helvetica">
                          <fo:inline font-weight="bold"><xsl:value-of select="rs_invoice/master_account/master_account_number"/></fo:inline>
                        </fo:block>
                      </fo:table-cell>
                    </fo:table-row>
                    <fo:table-row>
                      <fo:table-cell>
                        <fo:block/>
                      </fo:table-cell>
                      <fo:table-cell>
                        <fo:block font="10pt Helvetica">
                          <fo:inline font-weight="bold">Page <fo:page-number/> of <fo:page-number-citation ref-id="last-page"/></fo:inline>
                        </fo:block>
                      </fo:table-cell>
                      <fo:table-cell>
                        <fo:block/>
                      </fo:table-cell>
                    </fo:table-row>
                    <fo:table-row>
                      <fo:table-cell height="0.5cm" number-columns-spanned="4" text-align="center">
                        <fo:block font="10pt Helvetica">
                          <fo:inline font-weight="bold">Summary Of Services</fo:inline>
                        </fo:block>
                      </fo:table-cell>
                    </fo:table-row>
                  </fo:table-body>
                </fo:table>
              </fo:block>
            </fo:static-content>
            <fo:flow flow-name="xsl-region-body">
              <fo:block>
                <xsl:for-each select="rs_invoice/call_month">
                  <fo:table table-layout="fixed" width="80%">
                    <fo:table-column column-width="55%"/>
                    <fo:table-column column-width="14%"/>
                    <fo:table-column column-width="1%"/>
                    <fo:table-column column-width="14%"/>
                    <fo:table-column column-width="1%"/>
                    <fo:table-column column-width="15%"/>
                    <fo:table-header background-color="#7C9EDD">
                      <fo:table-row>
                        <fo:table-cell text-align="left" padding="0.1cm" number-columns-spanned="6" background-color="white">
                          <fo:block font="10pt Helvetica">
                            <fo:inline font-weight="bold">
                              <xsl:choose>
                                <xsl:when test="../master_account/frequency_code='M'">
                                  <fo:block/>
                                </xsl:when>
                                <xsl:otherwise>
                                  <xsl:value-of select="call_month_literal"/>
                                </xsl:otherwise>
                              </xsl:choose>
                            </fo:inline>
                          </fo:block>
                        </fo:table-cell>
                      </fo:table-row>
                      <fo:table-row>
                        <fo:table-cell text-align="left" padding="0.1cm" number-columns-spanned="6" >
                          <fo:block font="8pt Helvetica">
                            <fo:inline>Inbound Call Summary</fo:inline>
                          </fo:block>
                        </fo:table-cell>
                      </fo:table-row>
                      <fo:table-row>
                        <fo:table-cell text-align="left" padding-top="0.1cm" padding-left="0.1cm" padding-right="0.1cm">
                          <fo:block font="7pt Helvetica">
                            <fo:inline>Number Type</fo:inline>
                          </fo:block>
                        </fo:table-cell>
                        <fo:table-cell text-align="right" padding-top="0.1cm" padding-left="0.1cm" padding-right="0.1cm">
                          <fo:block font="7pt Helvetica">
                            <fo:inline>Number of Calls</fo:inline>
                          </fo:block>
                        </fo:table-cell>
                        <fo:table-cell>
                          <fo:block/>
                        </fo:table-cell>
                        <fo:table-cell text-align="right" padding-top="0.1cm" padding-left="0.1cm" padding-right="0.1cm">
                          <fo:block font="7pt Helvetica">
                            <fo:inline>Total Duration</fo:inline>
                          </fo:block>
                        </fo:table-cell>
                        <fo:table-cell>
                          <fo:block/>
                        </fo:table-cell>
                        <fo:table-cell text-align="right" padding-top="0.1cm" padding-left="0.1cm" padding-right="0.1cm">
                          <fo:block font="7pt Helvetica">
                            <fo:inline>Net Amount</fo:inline>
                          </fo:block>
                        </fo:table-cell>
                      </fo:table-row>
                      <fo:table-row>
                        <fo:table-cell text-align="left" number-columns-spanned="3" >
                          <fo:block/>
                        </fo:table-cell>
                        <fo:table-cell text-align="right" padding-bottom="0.1cm" padding-left="0.1cm" padding-right="0.1cm">
                          <fo:block font="7pt Helvetica">
                            <fo:inline>(mins)</fo:inline>
                          </fo:block>
                        </fo:table-cell>
                        <fo:table-cell>
                          <fo:block/>
                        </fo:table-cell>
                        <fo:table-cell text-align="right" padding-bottom="0.1cm" padding-left="0.1cm" padding-right="0.1cm">
                          <fo:block font="7pt Helvetica">
                            <fo:inline>£</fo:inline>
                          </fo:block>
                        </fo:table-cell>
                      </fo:table-row>
                    </fo:table-header>
                    <fo:table-body background-color="#CCDAF2">
                      <xsl:for-each select="invoice_line">
                        <fo:table-row>
                          <fo:table-cell text-align="left" padding-top="0.1cm" padding-left="0.1cm" padding-right="0.1cm">
                            <fo:block font="7pt Helvetica">
                              <fo:inline><xsl:value-of select="number_type"/></fo:inline>
                            </fo:block>
                          </fo:table-cell>
                          <fo:table-cell text-align="right" padding-top="0.1cm" padding-left="0.1cm" padding-right="0.1cm">
                            <fo:block font="7pt Helvetica">
                              <fo:inline><xsl:value-of select="total_calls"/></fo:inline>
                            </fo:block>
                          </fo:table-cell>
                          <fo:table-cell>
                            <fo:block/>
                          </fo:table-cell>
                          <fo:table-cell text-align="right" padding-top="0.1cm" padding-left="0.1cm" padding-right="0.1cm">
                            <fo:block font="7pt Helvetica">
                              <fo:inline><xsl:value-of select="total_duration"/></fo:inline>
                            </fo:block>
                          </fo:table-cell>
                          <fo:table-cell>
                            <fo:block/>
                          </fo:table-cell>
                          <fo:table-cell text-align="right" padding-top="0.1cm" padding-left="0.1cm" padding-right="0.1cm">
                            <fo:block font="7pt Helvetica">
                              <fo:inline><xsl:value-of select="net_amount"/></fo:inline>
                            </fo:block>
                          </fo:table-cell>
                        </fo:table-row>
                      </xsl:for-each>
                      <fo:table-row>
                        <fo:table-cell text-align="left" padding-top="0.1cm" padding-left="0.1cm" padding-right="0.1cm">
                          <fo:block/>
                        </fo:table-cell>
                        <fo:table-cell text-align="right" padding-top="0.1cm"
                          padding-left="0.1cm" padding-right="0.1cm" border-top="1pt solid black">
                          <fo:block font="7pt Helvetica">
                            <fo:inline><xsl:value-of select="sum_total_calls"/></fo:inline>
                          </fo:block>
                        </fo:table-cell>
                        <fo:table-cell>
                          <fo:block/>
                        </fo:table-cell>
                        <fo:table-cell text-align="right" padding-top="0.1cm"
                          padding-left="0.1cm" padding-right="0.1cm" border-top="1pt solid black">
                          <fo:block font="7pt Helvetica">
                            <fo:inline><xsl:value-of select="sum_total_duration"/></fo:inline>
                          </fo:block>
                        </fo:table-cell>
                        <fo:table-cell>
                          <fo:block/>
                        </fo:table-cell>
                        <fo:table-cell text-align="right" padding-top="0.1cm"
                          padding-left="0.1cm" padding-right="0.1cm" border-top="1pt solid black">
                          <fo:block font="7pt Helvetica">
                            <fo:inline><xsl:value-of select="sum_net_amount"/></fo:inline>
                          </fo:block>
                        </fo:table-cell>
                      </fo:table-row>
                      <fo:table-row>
                        <fo:table-cell height="0.5cm" text-align="left" padding="0.1cm" number-columns-spanned="6" background-color="white">
                          <fo:block/>
                        </fo:table-cell>
                      </fo:table-row>
                    </fo:table-body>
                  </fo:table>
                </xsl:for-each>
              </fo:block>
              <fo:block id="last-page"/>
            </fo:flow>
          </fo:page-sequence>
        </xsl:when>
        <xsl:otherwise/>
      </xsl:choose>
    </fo:root>
  </xsl:template>
</xsl:stylesheet>