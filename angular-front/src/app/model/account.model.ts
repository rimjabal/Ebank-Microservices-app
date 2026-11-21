export interface Account{
  id? : string,
  balance? : number,
  createdAt? : string,
  type? : string,
  customerId? : number
}
export enum  RequestStatus {
  SUCCESS=1, ERROR=2
}
export interface AccountListState {
  accounts? : Account[],
  status? : RequestStatus,
  errorMessage? : string
}
