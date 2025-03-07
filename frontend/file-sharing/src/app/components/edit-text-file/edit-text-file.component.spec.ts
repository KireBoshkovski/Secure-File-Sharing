import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditTextFileComponent } from './edit-text-file.component';

describe('EditTextFileComponent', () => {
  let component: EditTextFileComponent;
  let fixture: ComponentFixture<EditTextFileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditTextFileComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditTextFileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
