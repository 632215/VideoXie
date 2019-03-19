package com.weis.videoxie.bean;

public class HttpBean<T> {
    private int code;
    private String msg;
    private T data;
    private PageInfo pageInfo;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getErrcode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getResult() {
        return msg;
    }

    public void setResult(String errmsg) {
        this.msg = errmsg;
    }

    public class PageInfo {
        private int total;
        private int page;
        private int rows;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getRows() {
            return rows;
        }

        public void setRows(int rows) {
            this.rows = rows;
        }
    }
}
