import { Component, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AsyncPipe } from '@angular/common';
import { catchError, map, Observable, of, startWith } from 'rxjs';
import { Account, AccountListState, RequestStatus } from '../model/account.model';
import {LoadingService} from '../services/loading';

@Component({
  selector: 'app-accounts',
  standalone: true,
  imports: [AsyncPipe],
  templateUrl: './accounts.html',
  styleUrl: './accounts.css'
})
export class Accounts {
  private http = inject(HttpClient);
  public loadingService = inject(LoadingService)

  readonly RequestStatus = RequestStatus; // lets the template use the enum

  accounts$: Observable<AccountListState> = this.http
    .get<Account[]>("http://localhost:9999/EBANK-SERVICE/accounts")
    .pipe(
      map((resp): AccountListState => ({
        accounts: resp,
        status: RequestStatus.SUCCESS
      })),
      catchError(err => of<AccountListState>({
        accounts: [],
        status: RequestStatus.ERROR,
        errorMessage: err.statusText || err.message
      }))
    );
}
