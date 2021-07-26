export class CreditInfo {
  creditAmount: number;
  maturityDate: string;
  monthlyPayment: number;
  currentCreditBalance: number;

  constructor(props) {
    this.creditAmount = props.creditAmount;
    this.maturityDate = props.maturityDate;
    this.monthlyPayment = props.monthlyPayment;
    this.currentCreditBalance = props.currentCreditBalance;
  }
}
