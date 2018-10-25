package com.alphawizard.hdwallet.alphahdwallet.data.entiry;

import com.alphawizard.hdwallet.common.util.DiffUtilCallback;

import java.util.List;

public class Transaction {
    public int status;
    public String message;

    public List<TransactionBean> result ;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<TransactionBean> getmList() {
        return result;
    }

    public void setmList(List<TransactionBean> mList) {
        this.result = mList;
    }

    public static  class TransactionBean implements  DiffUtilCallback.DiffRule<TransactionBean>{

        String blockNumber;
        long timeStamp;
        String hash;
        String nonce;
        String blockHash;
        String transactionIndex;
        String from;
        String to;
        String value;
        String gas;
        String gasPrice;
        String isError;
        String txreceipt_status;
        String input;

        String contractAddress;  //+ error   mayby    because   null
        String cumulativeGasUsed;   // errror

        String gasUsed;
        String confirmations;

        public String getBlockNumber() {
            return blockNumber;
        }

        public void setBlockNumber(String blockNumber) {
            this.blockNumber = blockNumber;
        }

        public long getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(long timeStamp) {
            this.timeStamp = timeStamp;
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public String getNonce() {
            return nonce;
        }

        public void setNonce(String nonce) {
            this.nonce = nonce;
        }

        public String getBlockHash() {
            return blockHash;
        }

        public void setBlockHash(String blockHash) {
            this.blockHash = blockHash;
        }

        public String getTransactionIndex() {
            return transactionIndex;
        }

        public void setTransactionIndex(String transactionIndex) {
            this.transactionIndex = transactionIndex;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getGas() {
            return gas;
        }

        public void setGas(String gas) {
            this.gas = gas;
        }

        public String getGasPrice() {
            return gasPrice;
        }

        public void setGasPrice(String gasPrice) {
            this.gasPrice = gasPrice;
        }

        public String getIsError() {
            return isError;
        }

        public void setIsError(String isError) {
            this.isError = isError;
        }

        public String getTxreceipt_status() {
            return txreceipt_status;
        }

        public void setTxreceipt_status(String txreceipt_status) {
            this.txreceipt_status = txreceipt_status;
        }

        public String getInput() {
            return input;
        }

        public void setInput(String input) {
            this.input = input;
        }

        public String getContractAddress() {
            return contractAddress;
        }

        public void setContractAddress(String contractAddress) {
            this.contractAddress = contractAddress;
        }

        public String getCumulativeGasUsed() {
            return cumulativeGasUsed;
        }

        public void setCumulativeGasUsed(String cumulativeGasUsed) {
            this.cumulativeGasUsed = cumulativeGasUsed;
        }

        public String getGasUsed() {
            return gasUsed;
        }

        public void setGasUsed(String gasUsed) {
            this.gasUsed = gasUsed;
        }

        public String getConfirmations() {
            return confirmations;
        }

        public void setConfirmations(String confirmations) {
            this.confirmations = confirmations;
        }

        @Override
        public boolean isSame(TransactionBean bean) {
            return hash.equalsIgnoreCase(bean.getHash());
        }

        @Override
        public boolean isChange(TransactionBean bean) {
            return !hash.equalsIgnoreCase(bean.getHash());
        }
    }
}
