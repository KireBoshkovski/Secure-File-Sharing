import { Component, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-share-popup',
  standalone: false,
  templateUrl: './share-popup.component.html',
  styleUrl: './share-popup.component.css'
})
export class SharePopupComponent {
  @Output() close = new EventEmitter<void>();
  @Output() share = new EventEmitter<{ username: string, permission: string }>();

  username: string = '';
  selectedPermission: string = 'VIEW';

  onShare() {
    this.share.emit({ username: this.username, permission: this.selectedPermission });
  }

  onClose() {
    this.close.emit();
  }
}
