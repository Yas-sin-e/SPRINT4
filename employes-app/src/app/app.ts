import { RouterLink, RouterOutlet } from '@angular/router';
import { Component, effect, inject } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import Keycloak, { KeycloakProfile } from 'keycloak-js';

import {
  HasRolesDirective,
  KEYCLOAK_EVENT_SIGNAL,
  KeycloakEventType,
  typeEventArgs,
  ReadyArgs
} from 'keycloak-angular';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterModule, CommonModule,HasRolesDirective],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  public profile?: KeycloakProfile;
  authenticated = false;
  keycloakStatus: string | undefined;
  employeesDropdownOpen = false;
  userDropdownOpen = false;

  private readonly keycloak = inject(Keycloak);

  toggleEmployeesDropdown() {
    this.employeesDropdownOpen = !this.employeesDropdownOpen;
    this.userDropdownOpen = false;
  }

  toggleUserDropdown() {
    this.userDropdownOpen = !this.userDropdownOpen;
    this.employeesDropdownOpen = false;
  }

  closeDropdowns() {
    this.employeesDropdownOpen = false;
    this.userDropdownOpen = false;
  }

  private readonly keycloakSignal = inject(KEYCLOAK_EVENT_SIGNAL);

  constructor() {
    effect(() => {
      const keycloakEvent = this.keycloakSignal();

      this.keycloakStatus = keycloakEvent.type;

      if (keycloakEvent.type === KeycloakEventType.Ready) {
        this.authenticated = typeEventArgs<ReadyArgs>(keycloakEvent.args);
      }

      if (keycloakEvent.type === KeycloakEventType.AuthLogout) {
        this.authenticated = false;
      }
    });
  }

  login() {
    this.keycloak.login();
  }

  logout() {
    this.keycloak.logout();
  }

}
