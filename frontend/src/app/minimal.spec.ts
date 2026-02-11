
import { TestBed } from '@angular/core/testing';
import { Component } from '@angular/core';

@Component({ selector: 'test-cmp', template: '<p>Hello</p>', standalone: true })
class TestComponent {}

describe('Minimal', () => {
  it('should work', async () => {
    await TestBed.configureTestingModule({
      imports: [TestComponent],
    }).compileComponents();
    const fixture = TestBed.createComponent(TestComponent);
    expect(fixture.componentInstance).toBeTruthy();
  });
});
