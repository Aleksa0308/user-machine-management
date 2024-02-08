import {Component, OnInit, OnDestroy, ChangeDetectorRef} from '@angular/core';
import { LoginService } from './services/login.service';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, OnDestroy {
  title = 'frontend';
  isLoggedIn = false;
  items: any[] = [
    { label: 'Users', routerLink: '/users' },
    { label: 'Vacuums', routerLink: '/vacuums' },
    { label: 'Errors', routerLink: '/errors' },
  ];
  private authSubscription: Subscription | undefined;

  constructor(private loginService: LoginService, private router: Router) {}

  ngOnInit(): void {
    this.isLoggedIn = this.loginService.checkIfHasSavedToken();

    // Subscribe to authStatus changes
    this.authSubscription = this.loginService.authStatus.subscribe((loggedIn: boolean) => {
      this.isLoggedIn = loggedIn;
    });
  }

  ngOnDestroy(): void {
    // Unsubscribe to prevent memory leaks
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }

  logout(): void {
    this.loginService.logout();
    this.router.navigate(['/']);
  }
}
