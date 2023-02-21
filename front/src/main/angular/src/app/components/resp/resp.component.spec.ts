import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RespComponent } from './resp.component';

describe('RespComponent', () => {
  let component: RespComponent;
  let fixture: ComponentFixture<RespComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RespComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RespComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
