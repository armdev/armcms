/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frontend.web.utils;

import java.math.BigInteger;

/**
 *
 * @author Administrator
 */
public class PagingUtil {

  private int itemsPerPage;
  private BigInteger pagesInSection = null;
  private BigInteger currPage = null;
  private BigInteger pageCount = null;
  private Integer total;

  public PagingUtil(Integer currPage, Integer total, int itemsPerPage, int pagesInSection) {

    double count = Math.ceil((double) (total.intValue()) / itemsPerPage);
    Double d = new Double(count);
    int intCount = d.intValue();
    this.currPage = new BigInteger(String.valueOf(currPage));
    this.pageCount = new BigInteger(String.valueOf(intCount));
    this.total = total;
    this.itemsPerPage = itemsPerPage;
    this.pagesInSection = new BigInteger(String.valueOf(pagesInSection));
  }

  public int getPageCount() {
    return this.pageCount.intValue();
  }

  public Boolean hasPrevious() {
    if (currPage.intValue() > 1) {
      //&& (currPage.mod(pagesInSection).intValue() != 1)
      return true;
    } else {
      return false;
    }
  }

  public Boolean hasNext() {
    if (currPage.intValue() < getPageCount()) {
      //&& (currPage.mod(pagesInSection).intValue() != 0)
      return true;
    } else {
      return false;
    }
  }

  public int getPagesInSection() {
    return pagesInSection.intValue();
  }
}

