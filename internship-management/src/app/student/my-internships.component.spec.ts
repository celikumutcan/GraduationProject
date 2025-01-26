import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyInternshipsComponent } from './my-internships.component';

describe('MyInternshipsComponent', () => {
  let component: MyInternshipsComponent;
  let fixture: ComponentFixture<MyInternshipsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MyInternshipsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MyInternshipsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
