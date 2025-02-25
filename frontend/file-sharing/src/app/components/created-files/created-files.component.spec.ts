import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreatedFilesComponent } from './created-files.component';

describe('CreatedFilesComponent', () => {
  let component: CreatedFilesComponent;
  let fixture: ComponentFixture<CreatedFilesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CreatedFilesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreatedFilesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
